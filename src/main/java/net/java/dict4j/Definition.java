/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * Container class for a definition
 * 
 * @author ROTH Damien
 */
public class Definition
{
    /**
     * The dictionary where the definition come
     */
    private String dictionary = "";
    
    /**
     * The definition buffer
     */
    private StringBuffer definition;
    
    /**
     * Create a definition from a dictionary
     * @param dictionary
     */
    public Definition(String dictionary)
    {
        this.dictionary = dictionary;
        this.definition = new StringBuffer();
    }
    
    /**
     * Append a new line to the definition
     * @param str the line
     */
    public void append(String str)
    {
        this.definition.append(str).append("\n");
    }
    
    /**
     * Returns the definition
     * @return the definition
     */
    public String getDefinition()
    {
        return this.definition.toString();
    }
    
    /**
     * Returns the dictionary
     * @return the dictionary
     */
    public String getDictionary()
    {
        return this.dictionary;
    }
    
    /**
     * Sets a new dictionary's name
     * @param dictionary dictionary's name
     */
    public void setDictionary(String dictionary)
    {
        this.dictionary = dictionary;
    }
}
