/*
 * Dict4j, the OpenSource Java client for the DICT protocol
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package net.java.dict4j.data;

/**
 * A container class for the strategies
 * 
 * @author ROTH Damien
 */
public class Strategy {
    /**
     * The strategy code
     */
    private final String code;

    /**
     * The strategy name
     */
    private final String name;

    /**
     * Create an instance of the class with the code and the name of a strategy
     * 
     * @param code
     *            the code
     * @param name
     *            the name
     */
    public Strategy(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the strategy code
     * 
     * @return the strategy code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the strategy name
     * 
     * @return the strategy name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Strategy[code=" + code + ", name=" + name + "]";
    }
}
