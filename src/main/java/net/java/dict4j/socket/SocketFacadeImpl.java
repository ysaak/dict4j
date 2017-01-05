package net.java.dict4j.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketFacadeImpl implements SocketFacade {

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

    @Override
    public void connect(String host, int port, int timeout) throws IOException {
        this.socket = new Socket(host, port);
        socket.setSoTimeout(timeout);
        this.output = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
    }

    @Override
    public String readLine() throws IOException {
        return this.input.readLine();
    }

    @Override
    public void writeLine(String line) {
        this.output.println(line);
    }

    @Override
    public void close() throws IOException {

        try {
            if (this.output != null) {
                this.output.close();
            }
        }
        finally {
            try {
                if (this.input != null) {
                    this.input.close();
                }
            }
            finally {
                if (this.socket != null) {
                    this.socket.close();
                }
            }
        }

    }
}
