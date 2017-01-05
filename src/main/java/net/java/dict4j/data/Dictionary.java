/*
 * Dict4j, the OpenSource Java client for the DICT protocol
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.dict4j.data;

/**
 * A container class for the dictionaries
 * 
 * @author ROTH Damien
 */
public class Dictionary {
    public static final String FIRST_MATCH = "!";
    
    public static final String ALL_DICTIONARIES = "*";
    
    /**
     * The code of the dictionary
     */
    private final String code;

    /**
     * The name of the dictionary
     */
    private final String name;

    /**
     * Create an instance of the class with the code and the name of a
     * dictionary
     * 
     * @param code
     *            the code
     * @param name
     *            the name
     */
    public Dictionary(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the code of the dictionary
     * 
     * @return the code of the dictionary
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the name of the dictionary
     * 
     * @return the name of the dictionary
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Dictionary[code=" + code + ", name=" + name + "]";
    }
}
