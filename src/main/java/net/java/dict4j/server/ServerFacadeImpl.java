package net.java.dict4j.server;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.dict4j.data.Configuration;
import net.java.dict4j.data.ServerCapability;
import net.java.dict4j.socket.SocketFacade;
import net.java.dict4j.socket.SocketFacadeImpl;

public class ServerFacadeImpl implements ServerFacade {
    private final Logger logger = LoggerFactory.getLogger(ServerFacadeImpl.class);
    
    private static final Pattern CONNECT_LINE_PATTERN = Pattern.compile("^220 (.*) <(.*)> <(.*)>$");

    /**
     * Current session ID
     */
    private String authMsgId = "";

    /**
     * List of the special capacities of the server (auth, mime, ...)
     */
    private final Set<ServerCapability> capabilities = new HashSet<>();
    
    /**
     * Server banner
     */
    private String banner = "";

    /**
     * A boolean telling if we are currently connected to the DICT server.
     */
    private boolean connected = false;
    
    /**
     * Server connection configuration
     */
    private final Configuration configuration;
    
    private SocketFacade socketFacade;
    
    public ServerFacadeImpl(Configuration configuration) {
        this.configuration = configuration;
        this.socketFacade = new SocketFacadeImpl();
    }
    
    public void setSocketFacade(SocketFacade socketFacade) {
        if (connected) {
            // If connected, close old connection before switching facade
            try {
                close();
            }
            catch (IOException e) {
                logger.warn("Error while closing connection of the previous socket facade", e);
            }
        }
        
        this.socketFacade = socketFacade;
    }

    /**
     * Establish a connection to the DICT server
     * @throws ServerException
     * @return TRUE if the connection is open - throw DictException otherwise
     */
    public void connect() throws ServerException, IOException {
        if (!connected) {
            
            socketFacade.connect(this.configuration.getHost(), this.configuration.getPort(), this.configuration.getTimeout());

            final String fromServer = socketFacade.readLine(); // Server banner

            final Matcher m = CONNECT_LINE_PATTERN.matcher(fromServer);
            
            if (m.matches()) { // 220 = connect ok
                
                // Extract informations from the line
                this.banner = m.group(1);
                
                this.authMsgId = m.group(3);

                this.capabilities.clear();
                
                for (String capability : Arrays.asList(m.group(2).split("\\."))) {
                    ServerCapability sc = ServerCapability.getFromCode(capability);
                    if (sc != null) {
                        this.capabilities.add(sc);
                    }
                    else {
                        logger.warn("Server advertised an unknown capability : " + capability);
                    }
                }
                
                // Authenticate (if necessary)
                this.authenticate();

                this.connected = true;
                
                // Send client name
                this.sendClientCommand();

            }
            else {
                throw new ServerException(fromServer.substring(0, 3), fromServer);
            }
        }
    }

    private void authenticate() throws ServerException, IOException {
        if (this.configuration.getLogin() != null) {

            if (!this.capabilities.contains(ServerCapability.BASIC_AUTHENTICATION)) {
                throw new ServerException("502", "Command not implemented");
            }

            final String query;
            try {
                query = "AUTH " + this.configuration.getLogin() + " " + encodePassword(this.authMsgId + this.configuration.getPassword());
            }
            catch (NoSuchAlgorithmException e) {
                throw new ServerException("998", "Unable to encode password", e);
            }
            
            this.execute(query, "230");
        }
    }

    /**
     * Provide information to the server about the client name, for logging and
     * statistical purposes Automatically send during the connection
     */
    private void sendClientCommand() {
        if (this.configuration.getClientName() != null && this.configuration.getClientName().length() > 0) {
            try {
                this.execute("CLIENT " + this.configuration.getClientName(), null);
            }
            catch (ServerException | IOException e) {
                logger.error("Error while sending CLIENT command", e);
            }
        }
    }

    @Override
    public String getMessageId() {
        return this.authMsgId;
    }
    
    @Override
    public Set<ServerCapability> getCapabilities() {
        return this.capabilities;
    }
    
    @Override
    public String getServerBanner() {
        return this.banner;
    }
    
    @Override
    public boolean isConnectedToServer() {
        return this.connected;
    }

    @Override
    public List<String> execute(String query, String okResponse) throws ServerException, IOException {
        final List<String> lines = new ArrayList<>();

        int attempts = 0;
        String result;

        do {
            attempts++;
            // Connect to the server
            connect();

            // Send query
            socketFacade.writeLine(query);

            result = socketFacade.readLine();

            if (result == null) {
                if (attempts <= this.configuration.getReconnectionAttempts()) {
                    // The connection may be close, let's reconnect
                    this.connected = false;
                }
                else {
                    // Server unavailable
                    throw new ServerException("420", "Server temporarily unavailable");
                }
            }
        }
        while (result == null);

        if (okResponse != null && result.startsWith(okResponse)) {
            // OK - getting responses from the server
            boolean quit = false;
            
            if (result.startsWith("210")) {
                lines.add(result);
                quit = true;
            }

            while (!quit && (result = socketFacade.readLine()) != null) {
                if (result.startsWith("250")) {
                    quit = true;
                }
                else if (!".".equals(result)) {
                    lines.add(result);
                }
            }

        }
        else if (!result.startsWith("250")) {
            throw new ServerException(result.substring(0, 3), result);
        }

        return lines;
    }

    @Override
    public void close() throws IOException {
        String fromServer;
        boolean quit = false;

        if (!connected) {
            return;
        }

        try {
            socketFacade.writeLine("QUIT");

            // Clean the socket buffer
            while (!quit && (fromServer = socketFacade.readLine()) != null) {

                if (fromServer.startsWith("221")) { // Quit response
                    quit = true;
                }
            }
        }
        finally {
            socketFacade.close();
        }

        this.connected = false;
    }

    /**
     * Encode the password using the MD5 algorithm
     * 
     * @param password
     *            Password
     * @return Encoded password
     * @throws NoSuchAlgorithmException
     *             if MD5 algorithm isn't supported by the system
     */
    private String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte[] byteData = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }
}
