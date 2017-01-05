package net.java.dict4j.data;

public class Configuration {

    /**
     * The host name of the server: i.e. "dict.org"
     */
    private final String host;

    /**
     * The port used by the server. The default one for the DICT protocol is 2628.
     */
    private final int port;

    /**
     * The connection timeout
     */
    private int timeout = 5000;

    /**
     * The client name to announce
     */
    private String clientName = null;

    /**
     * Login to use for authentication
     */
    private String login = null;

    /**
     * Password to use for authentication
     */
    private String password = null;

    /**
     * Reconnection attempts
     */
    private int reconnectionAttempts = 1;
    
    public Configuration(String host) {
        this(host, 2628);
    }

    public Configuration(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReconnectionAttempts() {
        return reconnectionAttempts;
    }

    public void setReconnectionAttempts(int reconnectionAttempts) {
        this.reconnectionAttempts = reconnectionAttempts;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Configuration [host=" + host + ", port=" + port + ", timeout=" + timeout + ", clientName=" + clientName
                + ", login=" + login + ", password=" + password + ", reconnectionAttempts=" + reconnectionAttempts
                + "]";
    }
}
