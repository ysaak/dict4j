/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * A container class for the similar words from the match command
 * 
 * @author ROTH Damien
 */
public class MatchWord
{
    /**
     * The dictionary where the word can be found
     */
    private String dictionary;
    
    /**
     * The similar word
     */
    private String word;
    
    /**
     * Create an empty instance of the class
     */
    public MatchWord()
    {
        this.dictionary = "";
        this.word = "";
    }
    
    /**
     * Create an instance of the class with the line sended by the server
     * @param serverLine line sended bu the dict server
     */
    public MatchWord(String serverLine)
    {
        String[] temp = serverLine.split(" ", 2);
        
        this.dictionary = temp[0];
        this.word = temp[1].substring(1, temp[1].length() - 1);
    }
    
    /**
     * Create an instance of the class with the dictionary and the word
     * @param code the dictionary
     * @param name the word
     */
    public MatchWord(String dict, String word)
    {
        this.dictionary = dict;
        this.word = word;
    }
    
    /**
     * Defines the dictionary where the word can be found
     * @param d the dictionary
     */
    public void setDictionary(String d)
    {
        this.dictionary = d;
    }
    
    /**
     * Defines the similar word
     * @param w the word
     */
    public void setWord(String w)
    {
        this.word = w;
    }
    
    /**
     * Returns the dictionary where the word can be found
     * @return the dictionary where the word can be found
     */
    public String getDictionary()
    {
        return this.dictionary;
    }
    
    /**
     * Returns the similar word
     * @return the similar word
     */
    public String getWord()
    {
        return this.word;
    }
}
