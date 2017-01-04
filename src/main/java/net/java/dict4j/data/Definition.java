/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j.data;

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
    private final Dictionary dictionary;
    
    /**
     * Word
     */
    private final String word;
    
    /**
     * The definition
     */
    private final String definition;

    public Definition(Dictionary dictionary, String word, String definition) {
        this.dictionary = dictionary;
        this.word = word;
        this.definition = definition;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "Definition[dictionary=" + dictionary + ", word=" + word + ", definition=" + definition + "]";
    }
}
