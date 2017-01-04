package net.java.dict4j.server;

/**
 * Exception class managing the basics dict server errors.
 * 
 * @author LITZELMANN Cedric
 * @author ROTH Damien
 */
public class ServerException extends Exception {
    /**
     * Generated serial ID
     */
    private static final long serialVersionUID = 3084766495682420520L;

    /**
     * The DICT error code returned by the server
     */
    private int code;

    public ServerException(String error, String message) {
        super(message);
        this.code = Integer.parseInt(error);
    }

    public ServerException(String error, String message, Throwable cause) {
        super(message, cause);
        this.code = Integer.parseInt(error);
    }

    /**
     * Return the error code
     * 
     * @return the error code
     */
    public int getErrorCode() {
        return this.code;
    }
}
