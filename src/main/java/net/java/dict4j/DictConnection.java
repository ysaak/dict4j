/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

/**
 * Layer abstraction of a dict server 
 * 
 * @author LITZELMANN Cedric
 * @author ROTH Damien
 */
public class DictConnection
{
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
     * The name of the client who which to connect to the server
     */
    private String clientName = "";
    
    /**
     * The timeout 
     */
    private int timeout = 5000;
    
    /**
     * The socket used to connect to the DICT server.
     */
    private Socket socket;

    /**
     * A output stream piped to the socket in order to send command to the server.
     */
    private PrintWriter out;

    /**
     * A input stream piped to the socket in order to receive messages from the server.
     */
    private BufferedReader in;
    
    /**
     * A boolean telling if we are currently connected to the DICT server.
     */
    private boolean connected;
    
    /**
     * The list of all the databases hosted by the server. Each database
     * correspond to a dictionnary.
     */
    private List<Dictionary> dictionariesList = null;
    
    /**
     * The list of all the strategies hosted by the server.
     */
    private List<Strategy> strategiesList = null;
    
    /**
     * Current session ID
     */
    private String authMsgId = "";
    
    /**
     * List of the special capacities of the server (auth, mime, ...)
     */
    private List<String> capacities = null;
    
    /**
     * Initialize a basic instance with predefined settings
     */
    public DictConnection()
    {
        this("dict.org", 2628);
    }
    
    /**
     * Initialize a basic instance and set th host
     * @param host Host
     */
    public DictConnection(String host)
    {
        this(host, 2628);
    }
    
    /**
     * Initialize an instance and set the host and the port
     * @param host Host
     * @param port Port
     */
    public DictConnection(String host, int port)
    {
        this.host = host;
        this.port = port;
        this.connected = false;
        this.socket = null;
        this.out = null;
        this.in = null;
    }
    
    
    /**
     * Establish a connexion to the dict server
     * @throws DictException
     * @return TRUE if the connection is open - throw DictException otherwise
     */
    public void connect() throws DictException
    {
        String fromServer;
        
        if (this.isConnected())
        {
            return;
        }
        
        try
        {
            this.socket = new Socket(this.host, this.port);
            this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),
                        "UTF-8"), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),
                        "UTF-8"));
            
            fromServer = this.in.readLine(); // Server banner
            
            if (fromServer.startsWith("220"))
            {   // 220 = connect ok
                
                // Line format : code infos <(CAPACITIES)> (<MSG-ID>)
                String[] temp = fromServer.split(" ");
                this.authMsgId = temp[temp.length - 1];
                this.capacities = Arrays.asList(temp[temp.length-2].split("."));
                
                this.connected = true;
                this.sendClientCommand();
                return;
            }
            else
            {
                throw new DictException(fromServer.substring(0, 3));
            }
        }
        catch(UnknownHostException uhe)
        {
            throw new DictException(uhe);
        }
        catch(IOException ioe)
        {
            throw new DictException(ioe);
        }
    }
    
    /**
     * Close the actual connexion
     * @throws DictException
     */
    public void close() throws DictException
    {
        String fromServer;
        boolean quit = false;
        
        if (!this.isConnected())
        {
            return;
        }
        
        try
        {
            this.out.println("QUIT");
            
            // Clean the socket buffer
            while (quit == false && (fromServer = this.in.readLine()) != null)
            {
                if (fromServer.startsWith("221"))
                { // Quit response
                    quit = true;
                }
            }
            
            this.out.close();
            this.in.close();
            this.socket.close();
            
            this.connected = false;
        }
        catch (IOException ioe)
        {
            throw new DictException(ioe);
        }
    }
    
    /**
     * Get the dictionaries' list from the server
     * @throws DictException
     * @return the dictionaries list
     */
    public List<Dictionary> getDictionaries() throws DictException
    {
        if (this.dictionariesList == null)
        {
            String fromServer;
            boolean quit = false;
            this.dictionariesList = new ArrayList<Dictionary>();
            this.connect();
            
            try
            {
                fromServer = this.query("SHOW DB");
                
                if (fromServer.startsWith("110"))
                {   // OK - getting responses from the server
                    while (quit == false && (fromServer = this.in.readLine()) != null)
                    {
                        if (fromServer.startsWith("250"))
                        {
                            quit = true;
                        }
                        else if (!fromServer.equals("."))
                        {
                            this.dictionariesList.add(new Dictionary(fromServer));
                        }
                    }
                }
                else
                {
                    throw new DictException(fromServer.substring(0,3));
                }
            }
            catch (IOException ioe)
            {
                throw new DictException(ioe);
            }
        }
        return this.dictionariesList;
    }
    
    /**
     * Get the strategies allowed by the server for the MATCH command
     * @throws DictException
     * @return the strategies list
     */
    public List<Strategy> getStrategies() throws DictException
    {
        if (this.strategiesList == null)
        {
            String fromServer;
            boolean quit = false;
            this.strategiesList = new ArrayList<Strategy>();
            this.connect();
            
            try
            {
                fromServer = this.query("SHOW STRAT");
                
                if (fromServer.startsWith("111"))
                {   // OK - getting responses from the server
                    while (quit == false && (fromServer = this.in.readLine()) != null)
                    {
                        if (fromServer.startsWith("250"))
                        {
                            quit = true;
                        }
                        else if (!fromServer.equals("."))
                        {
                            this.strategiesList.add(new Strategy(fromServer));
                        }
                    }
                }
                else
                {
                    throw new DictException(fromServer.substring(0,3));
                }
            }
            catch (IOException ioe)
            {
                throw new DictException(ioe);
            }
        }
        
        return this.strategiesList;
    }
    
    
    /**
     * Get the definition of a word
     * @param database the database in which the word will be searched
     * @param word the search word
     * @throws DictException
     * @return a list of all the definitions found
     */
    public List<Definition> define(String database, String word)
        throws DictException
    {
        String fromServer;
        boolean quit = false;
        String[] test;
        this.connect();
        
        List<Definition> result = new ArrayList<Definition>();
        int index = -1;
        
        try
        {
            fromServer = this.query("DEFINE " + database + " " + word);
            
            if (fromServer.startsWith("150"))
            {
                while (quit == false && (fromServer = this.in.readLine()) != null)
                {
                    if (fromServer.startsWith("151"))
                    {   // First line - Contains the DB Name
                        index++;
                        test = fromServer.split(" ", 4);
                        result.add(new Definition(
                                test[3].substring(1, test[3].length() - 1)));
                    }
                    else if (fromServer.startsWith("250"))
                    {   // End of the request
                        quit = true;
                    }
                    else if (!fromServer.equals("."))
                    {
                        result.get(index).append(fromServer);
                    }
                }
            }
            else
            {
                throw new DictException(fromServer.substring(0,3));
            }
        }
        catch (IOException ioe)
        {
            throw new DictException(ioe);
        }
        
        return result;
    }
    
    /**
     * Get words that match with a strategie from a word
     * @param database the database in which the words will be searched
     * @param word the base word
     * @param strategy the strategies used
     * @throws DictException
     * @return a list containing the neighbours words
     */
    public List<MatchWord> match(String database, String word, String strategy)
        throws DictException
    {
        List<MatchWord> result = new ArrayList<MatchWord>();
        String fromServer;
        boolean quit = false;
        this.connect();
        
        try
        {
            fromServer = this.query("MATCH " + database + " " + strategy + " " + word);
            
            if (fromServer.startsWith("152"))
            {
                while (quit == false && (fromServer = this.in.readLine()) != null)
                {
                    if (fromServer.startsWith("250"))
                    {
                        quit = true;
                    }
                    else if (!fromServer.equals("."))
                    {
                        result.add(new MatchWord(fromServer)); 
                    }
                }
            }
            else
            {
                throw new DictException(fromServer.substring(0,3));
            }
        }
        catch (IOException ioe)
        {
            throw new DictException(ioe);
        }
        
        return result;
    }
    
    /**
     * Defines the name of the client who wants to connect to the server 
     * @param clientName the client's name
     */
    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }
    
    /**
     * Returns the name of the client who wants to connect to the server
     * @return the name of the client who wants to connect to the server
     */
    public String getClientName()
    {
        return this.clientName;
    }

    /**
     * Provide information to the server about the clientname, for logging and statistical purposes
     * Automatically send during the connection
     * 
     * @param clientname Client name
     * @throws DictException
     */
    private void sendClientCommand()
        throws DictException 
    {
        if (this.clientName.length() > 0 || !isConnected())
        {
            return;
        }
        
        String fromServer;
        
        fromServer = this.query("CLIENT " + this.clientName);
        
        // 250 code is the only possible answer
        if (!fromServer.startsWith("250"))
        {
            throw new DictException(fromServer.substring(0, 3));
        }
    }
    
    /**
     * Set the host
     * @param newHost host address
     */
    public void setHost(String newHost) throws DictException
    {
        if (isUrl(newHost))
        {
            this.host = newHost;
        }
        else 
        {
            throw new DictException(900, "Host URL is incorrect");
        }
    }
    
    /**
     * Set the host port
     * @param newPort Port
     */
    public void setPort(int newPort)
    {
        this.port = newPort;
    }

    /**
     * Return the host
     * @return return the host
     */
    public String getHost()
    {
        return this.host;
    }
    
    /**
     * Return the port
     * @return return the port
     */
    public int getPort()
    {
        return this.port;
    }
    
    /**
     * Gets the dictionary name from the databases list
     * @param code Dictionary code
     * @return the dictionary name
     * @throws DictException
     */
    public String getDictionaryName(String code)
        throws DictException
    {
        Dictionary dictionary;
        
        // First, we check if the code is a special code
        // Checks the RFC-2229 for more details
        if (code.equals("*"))
        {
            return "Any dictionary";
        }
        else if (code.equals("!"))
        {
            return "First match";
        }
        
        // Gets the databases list
        if (this.dictionariesList == null)
        {
            getDictionaries();
        }
        
        // Look down the databases list to get the name 
        for (int i=0; i<this.dictionariesList.size(); i++)
        {
            dictionary = this.dictionariesList.get(i); 
            
            if (dictionary.getCode().equals(code))
            { 
                return dictionary.getName();
            }
        }
        
        // If the name isn't in the list, return null
        return null;
    }
    
    /**
     * Check if we are connected to the server
     * @return true if we are connected - false otherwise
     */
    public boolean isConnected()
    {
        return this.connected;
    }

    /**
     * Check if the URL is correct and a server exists
     * @param url an Url
     * @return true if everything is ok - false otherwise
     */
    public static boolean isUrl(String url)
    {
        boolean ok;
        if (url == null)
        {
            return false;
        }
        
        try
        {
            InetAddress.getByName(url);
            ok = true;
        }
        catch (UnknownHostException uhex)
        {
            ok = false;
        }
        
        return ok;
    }
    
    /**
     * Executes a query and deals with the automatic deconnexion
     * @param query A query to send to the server
     * @return The first ligne of the response from the server
     * @throws DictException
     */
    private String query(String query)
        throws DictException
    {
        String result = null;

        this.out.println(query);
        
        try
        {
            result = in.readLine();

            if (result == null)
            {
                // The connexion may be close, reconnexion
                this.connected = false;
                this.connect();
    
                this.out.println(query);
                result = in.readLine();
    
                if (result == null)
                {
                    // If result is still equal to null, the server is unavailable
                    // We send the appropriate exception
                    throw new DictException(420);
                }
            }
        }
        catch (IOException ioe)
        {
            throw new DictException(ioe);
        }

        return result;
    }
    
    /**
     * Checks if the server is reachable (ping)
     * @return TRUE if the server is reachable - FALSE otherwise
     * @throws DictException
     */
    public boolean isAvailable()
        throws DictException
    {
        boolean result = false;
        
        try
        {
            result = InetAddress.getByName(this.host).isReachable(this.timeout);
        }
        catch (Exception e)
        {
            throw new DictException(e);
        }
        
        return result;
    }
    
    /**
     * Log into the server
     * @param username the username
     * @param password the password
     * @throws DictException
     */
    public void login(String username, String password)
        throws DictException
    {
        this.connect();
        
        if (!this.capacities.contains("auth"))
        {
            throw new DictException(DictReturnCode.COMMAND_NOT_IMPLEMENTED);
        }

        String query = "AUTH " + username + " "
            + cryptMD5(this.authMsgId + password);
        
        String fromServer = this.query(query);
        
        // Return code : 
        // 230 Authentication successful
        // 531 Access denied
        if (fromServer.startsWith("531"))
        {
            throw new DictException(531, fromServer.substring(4));
        }
    }
    
    /**
     * Crypt a string with the MD5 algorythm
     * @param value value to crypt
     * @return the value hashed with the MD5 algorythm
     * @throws DictException
     */
    private String cryptMD5(String value)
        throws DictException
    {
        MessageDigest md5 = null;
        
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new DictException(e);
        }
        
        byte[] bytes = md5.digest(value.getBytes());
        
        // Convert the byte array into a hex string
        String hex = "";
        char[] hexChars ={'0','1','2','3','4','5','6','7',
                '8','9','A','B','C','D','E','F'};
        int msb;
        int lsb = 0;
        
        // MSB maps to idx 0
        for (int i=0; i<bytes.length; i++)
        {
            msb = ((int)bytes[i] & 0x000000FF) / 16;
            lsb = ((int)bytes[i] & 0x000000FF) % 16;
            hex += hexChars[msb] + hexChars[lsb];
        }

        return hex;
    }
}
