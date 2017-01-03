/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * A container class for the strategies
 * 
 * @author ROTH Damien
 */
public class Strategy
{
    /**
     * The strategy code
     */
    private String code;
    
    /**
     * The strategy name
     */
    private String name;
    
    /**
     * Create an empty instance of the class
     */
    public Strategy()
    {
        this.code = "";
        this.name = "";
    }
    
    /**
     * Create an instance of the class with the line sended by the server
     * @param serverLine line sended by the dict server
     */
    public Strategy(String serverLine)
    {
        String[] data = serverLine.split(" ", 2);
        this.code = data[0];
        this.name = data[1];
    }
    
    /**
     * Create an instance of the class with the code and the name of a strategy
     * @param code the code
     * @param name the name
     */
    public Strategy(String code, String name)
    {
        this.code = code;
        this.name = name;
    }
    
    /**
     * Defines the new strategy code
     * @param c the code
     */
    public void setCode(String c)
    {
        this.code = c;
    }
    
    /**
     * Defines the new strategy name
     * @param n the name
     */
    public void setName(String n)
    {
        this.name = n;
    }
    
    /**
     * Returns the strategy code
     * @return the strategy code
     */
    public String getCode()
    {
        return this.code;
    }
    
    /**
     * Returns the strategy name
     * @return the strategy name
     */
    public String getName()
    {
        return this.name;
    }
}
