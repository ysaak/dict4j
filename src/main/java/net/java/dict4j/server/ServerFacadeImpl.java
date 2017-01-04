package net.java.dict4j.server;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.java.dict4j.data.Configuration;
import net.java.dict4j.socket.SocketFacade;
import net.java.dict4j.socket.SocketFacadeImpl;

public class ServerFacadeImpl implements ServerFacade {

    /**
     * Current session ID
     */
    private String authMsgId = "";

    /**
     * List of the special capacities of the server (auth, mime, ...)
     */
    private final Set<String> capacities = new HashSet<>();

    /**
     * A boolean telling if we are currently connected to the DICT server.
     */
    private boolean connected = false;
    
    /**
     * Server connection configuration
     */
    private Configuration configuration = new Configuration("dict.org");
    
    private SocketFacade socketFacade;
    
    public ServerFacadeImpl() {
        this.socketFacade = new SocketFacadeImpl();
    }
    
    public void setSocketFacade(SocketFacade socketFacade) {
        if (connected) {
            // If connected, close old connection before switching facade
            try {
                close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        this.socketFacade = socketFacade;
    }

    @Override
    public void configure(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void connect() throws ServerException, IOException {
        String fromServer;

        if (!connected) {
            
            socketFacade.connect(this.configuration.getHost(), this.configuration.getPort(), this.configuration.getTimeout());

            fromServer = socketFacade.readLine(); // Server banner

            System.out.println("connect > " + fromServer);

            if (fromServer.startsWith("220")) { // 220 = connect ok

                // Line format : code informations <(CAPACITIES)> (<MSG-ID>)
                String[] temp = fromServer.split(" ");
                this.authMsgId = temp[temp.length - 1];
                
                this.capacities.clear();
                this.capacities.addAll(Arrays.asList(temp[temp.length - 2].split(".")));

                
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

            if (!this.capacities.contains("auth")) {
                throw new ServerException("502", "Command not implemented");
            }

            final String query;
            try {
                query = "AUTH " + this.configuration.getLogin() + " " + encodePassword(this.authMsgId + this.configuration.getPassword());
            }
            catch (NoSuchAlgorithmException e) {
                throw new ServerException("998", "Unable to encode password", e);
            }
            
            this.query(query, "230");
        }
    }

    /**
     * Provide information to the server about the clientname, for logging and
     * statistical purposes Automatically send during the connection
     */
    private void sendClientCommand() {
        if (this.configuration.getClientName() != null && this.configuration.getClientName().length() > 0) {
            try {
                this.query("CLIENT " + this.configuration.getClientName(), null);
            }
            catch (ServerException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String getAuthMsgId() {
        return authMsgId;
    }

    @Override
    public List<String> query(String query, String okResponse) throws ServerException, IOException {
        final List<String> lines = new ArrayList<>();

        int attempts = 0;
        String result = null;

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

        System.out.println("query > " + result);

        if (okResponse != null && result.startsWith(okResponse)) {
            // OK - getting responses from the server
            boolean quit = false;
            
            if (result.startsWith("210")) {
                lines.add(result);
                quit = true;
            }

            while (quit == false && (result = socketFacade.readLine()) != null) {
                System.out.println("query > " + result);
                if (result.startsWith("250")) {
                    quit = true;
                }
                else if (!result.equals(".")) {
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

        if (connected) {

            try {
                socketFacade.writeLine("QUIT");
    
                // Clean the socket buffer
                while (quit == false && (fromServer = socketFacade.readLine()) != null) {
    
                    System.out.println("close > " + fromServer);
    
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

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }
}
