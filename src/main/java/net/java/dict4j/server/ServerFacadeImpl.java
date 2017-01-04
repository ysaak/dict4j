package net.java.dict4j.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerFacadeImpl implements ServerFacade {

    private int reconnectionAttempts = 1;

    /**
     * The host name of the server: i.e. "dict.org"
     */
    private String host;

    /**
     * The port used by the server. The default one for the DICT protocol is
     * 2628.
     */
    private int port;

    /**
     * The socket used to connect to the DICT server.
     */
    private Socket socket = null;

    /**
     * A output stream piped to the socket in order to send command to the
     * server.
     */
    private PrintWriter output = null;

    /**
     * A input stream piped to the socket in order to receive messages from the
     * server.
     */
    private BufferedReader input = null;

    /**
     * The timeout
     */
    private int timeout = 5000;

    private String clientName = "Ysaak's dict4j";

    private String login = null;

    private String password = null;

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

    @Override
    public void initialize(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() throws ServerException, IOException {
        String fromServer;

        if (!connected) {
            this.socket = new Socket(this.host, this.port);
            socket.setSoTimeout(timeout);
            this.output = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));

            fromServer = this.input.readLine(); // Server banner

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
        if (login != null) {

            if (!this.capacities.contains("auth")) {
                throw new ServerException("502", "Command not implemented");
            }

            final String query;
            try {
                query = "AUTH " + login + " " + encodePassword(this.authMsgId + password);
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
        if (this.clientName != null && this.clientName.length() > 0) {
            try {
                this.query("CLIENT " + clientName, null);
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
            this.output.println(query);

            result = this.input.readLine();

            if (result == null) {
                if (attempts <= reconnectionAttempts) {
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

            while (quit == false && (result = this.input.readLine()) != null) {
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

            this.output.println("QUIT");

            // Clean the socket buffer
            while (quit == false && (fromServer = this.input.readLine()) != null) {

                System.out.println("close > " + fromServer);

                if (fromServer.startsWith("221")) { // Quit response
                    quit = true;
                }
            }

            this.output.close();
            this.input.close();
            this.socket.close();

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
