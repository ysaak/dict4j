/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * A container class for the dictionaries
 * 
 * @author ROTH Damien
 */
public class Dictionary
{
    /**
     * The code of the dictionary
     */
    private String code;
    
    /**
     * The name of the dictionary
     */
    private String name;
    
    /**
     * Create an empty instance of the class
     */
    public Dictionary()
    {
        this.code = "";
        this.name = "";
    }
    
    /**
     * Create an instance of the class with the line sended by the server
     * @param serverLine line sended bu the dict server
     */
    public Dictionary(String serverLine)
    {
        String[] data = serverLine.split(" ", 2);
        this.code = data[0];
        this.name = data[1].replace("\"", "");
    }
    
    /**
     * Create an instance of the class with the code and the name of a dictionary
     * @param code the code
     * @param name the name
     */
    public Dictionary(String code, String name)
    {
        this.code = code;
        this.name = name;
    }
    
    /**
     * Defines a new code for the dictionary
     * @param c new code
     */
    public void setCode(String c)
    {
        this.code = c;
    }
    
    /**
     * Defines a new name for the dictionary
     * @param n new name
     */
    public void setName(String n)
    {
        this.name = n;
    }
    
    /**
     * Returns the code of the dictionary
     * @return the code of the dictionary
     */
    public String getCode()
    {
        return this.code;
    }
    
    /**
     * Returns the name of the dictionary
     * @return the name of the dictionary
     */
    public String getName()
    {
        return this.name;
    }
}
