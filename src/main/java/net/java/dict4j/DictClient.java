package net.java.dict4j;

import java.io.Closeable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.dict4j.data.Configuration;
import net.java.dict4j.data.Definition;
import net.java.dict4j.data.Dictionary;
import net.java.dict4j.data.Strategy;
import net.java.dict4j.exception.DictError;
import net.java.dict4j.exception.InvalidDictionaryException;
import net.java.dict4j.exception.InvalidStrategyException;
import net.java.dict4j.exception.NoDictionaryPresentException;
import net.java.dict4j.exception.NoMatchException;
import net.java.dict4j.exception.NoStrategyPresentException;
import net.java.dict4j.exception.UnexpectedServerException;
import net.java.dict4j.exception.server.AccessDeniedException;
import net.java.dict4j.exception.server.ServerShuttingDownException;
import net.java.dict4j.exception.server.ServerTemporaryNotAvailableException;
import net.java.dict4j.exception.technical.CommandNotImplementedException;
import net.java.dict4j.exception.technical.ParameterNotImplementedException;
import net.java.dict4j.exception.technical.SyntaxErrorCommandException;
import net.java.dict4j.exception.technical.SyntaxErrorParametersException;
import net.java.dict4j.server.ServerException;
import net.java.dict4j.server.ServerFacade;
import net.java.dict4j.server.ServerFacadeImpl;

public class DictClient implements Closeable {
    private final Logger logger = LoggerFactory.getLogger(DictClient.class);

    private Configuration configuration;
    
    private ServerFacade serverFacade;

    public DictClient(String host) {
        this(new Configuration(host));
    }

    public DictClient(String host, int port) {
        this(new Configuration(host, port));
    }

    public DictClient(Configuration configuration) {
        this.configuration = configuration;
        this.serverFacade = new ServerFacadeImpl();
        this.serverFacade.configure(configuration);
    }
    
    public void setServerFacade(ServerFacade serverFacade) {
        if (this.serverFacade != null) {
            try {
                this.serverFacade.close();
            }
            catch (IOException e) {
                logger.warn("Error while closing the connection of the previous server facade", e);
            }
        }
        
        this.serverFacade = serverFacade;
        this.serverFacade.configure(configuration);
    }
    
    /**
     * Get the dictionaries list from the server
     * 
     * @throws ServerException
     * @return the dictionaries list
     * @throws NoDictionaryPresentException 
     * @throws IOException 
     */
    public List<Dictionary> getDictionnaries() throws NoDictionaryPresentException, IOException {

        final List<Dictionary> dictionaries = new ArrayList<>();

        final List<String> response;
        
        try {
            response = serverFacade.query("SHOW DB", "110");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            propagateException(e, NoDictionaryPresentException.class);
            throw new UnexpectedServerException(e);
        }

        String code;
        String name;

        for (String serverLine : response) {
            final String[] data = serverLine.split(" ", 2);

            code = data[0];
            name = data[1].replace("\"", "");

            dictionaries.add(new Dictionary(code, name));
        }

        return dictionaries;
    }

    /**
     * Get the strategies allowed by the server for the MATCH command
     * 
     * @throws ServerException
     * @return the strategies list
     * @throws NoStrategyPresentException 
     * @throws IOException 
     */
    public List<Strategy> getStrategies() throws NoStrategyPresentException, IOException {
        final List<Strategy> strategies = new ArrayList<>();

        final List<String> response;
        try {
            response = serverFacade.query("SHOW STRAT", "111");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            propagateException(e, NoStrategyPresentException.class);
            throw new UnexpectedServerException(e);
        }

        String code;
        String name;

        for (String serverLine : response) {
            final String[] data = serverLine.split(" ", 2);

            code = data[0];
            name = data[1];

            strategies.add(new Strategy(code, name));
        }

        return strategies;
    }

    @Override
    public void close() throws IOException {
        serverFacade.close();
    }

    /**
     * 
     * @param dictionary
     * @param word
     * @return
     * @throws NoMatchException 
     * @throws InvalidDictionaryException 
     * @throws IOException 
     */
    public List<Definition> define(Dictionary dictionary, String word) throws InvalidDictionaryException, NoMatchException, IOException {
        return define(dictionary.getCode(), word);
    }

    /**
     * 
     * @param dictionaryCode
     * @param word
     * @return
     * @throws InvalidDictionaryException 
     * @throws NoMatchException 
     * @throws IOException 
     */
    public List<Definition> define(String dictionaryCode, String word) throws InvalidDictionaryException, NoMatchException, IOException {
        final List<String> response;
        try {
            response = serverFacade.query("DEFINE " + dictionaryCode + " \"" + word + "\"", "150");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            propagateException(e, InvalidDictionaryException.class);
            propagateException(e, NoMatchException.class);
            throw new UnexpectedServerException(e);
        }

        final Pattern pattern = Pattern.compile("^151 \"(.*)\" (.*) \"(.*)\"$");

        List<Definition> definitions = new ArrayList<>();

        final StringBuilder builder = new StringBuilder();
        String definedWord = null;
        Dictionary dictionary = null;
        Matcher matcher;

        for (String line : response) {

            if (line.startsWith("151") && (matcher = pattern.matcher(line)).matches()) {

                if (definedWord != null) {
                    // Store definition
                    definitions.add(new Definition(dictionary, definedWord, builder.toString()));
                }

                definedWord = matcher.group(1);
                dictionary = new Dictionary(matcher.group(2), matcher.group(3));
                builder.setLength(0);
            }
            else {
                builder.append(line);
            }
        }

        if (definedWord != null) {
            // Store definition
            definitions.add(new Definition(dictionary, definedWord, builder.toString()));
        }

        return definitions;
    }

    public List<String> match(Dictionary dictionary, Strategy strategy, String word) throws InvalidDictionaryException, InvalidStrategyException, NoMatchException, IOException {
        return match(dictionary.getCode(), strategy.getCode(), word);
    }

    public List<String> match(String dictionaryCode, String strategyCode, String word) throws InvalidDictionaryException, InvalidStrategyException, NoMatchException, IOException {
        final List<String> response;
        try {
            response = serverFacade.query("MATCH " + dictionaryCode + " " + strategyCode + " \"" + word + "\"", "152");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            propagateException(e, InvalidDictionaryException.class);
            propagateException(e, InvalidStrategyException.class);
            propagateException(e, NoMatchException.class);
            throw new UnexpectedServerException(e);
        }

        final List<String> matchedWords = new ArrayList<>();

        final Pattern pattern = Pattern.compile("^(.*) \"(.*)\"$");
        Matcher matcher;

        for (String line : response) {
            if ((matcher = pattern.matcher(line)).matches()) {
                matchedWords.add(matcher.group(2));
            }
        }

        return matchedWords;
    }

    public String getServerInformation() throws IOException {
        final List<String> response;
        try {
            response = serverFacade.query("SHOW SERVER", "114");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            throw new UnexpectedServerException(e);
        }

        final StringBuilder informations = new StringBuilder();

        for (String line : response) {
            informations.append(line).append("\n");
        }

        return informations.toString();
    }

    public String getServerStatus() throws IOException {
        final List<String> response;
        try {
            response = serverFacade.query("STATUS", "210");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            throw new UnexpectedServerException(e);
        }

        String statusLine = "";

        if (response != null && !response.isEmpty()) {

            statusLine = response.get(0).substring(4);

        }

        return statusLine;
    }

    public String getDictionaryInformation(Dictionary dictionary) throws InvalidDictionaryException, IOException {
        return getDictionaryInformation(dictionary.getCode());
    }

    public String getDictionaryInformation(String dictionaryCode) throws InvalidDictionaryException, IOException {
        final List<String> response;
        try {
            response = serverFacade.query("SHOW INFO " + dictionaryCode, "112");
        }
        catch (ServerException e) {
            propagateGeneralException(e);
            propagateException(e, InvalidDictionaryException.class);
            throw new UnexpectedServerException(e);
        }

        final StringBuilder informations = new StringBuilder();

        for (String line : response) {
            informations.append(line).append("\n");
        }

        return informations.toString();
    }
    

    private <T extends Exception> void propagateException(ServerException e, Class<T> clazz) throws T {
        int expectedCode = 0;
        
        if (clazz.isAnnotationPresent(DictError.class)) {
            final Annotation annotation = clazz.getAnnotation(DictError.class);
            final DictError dictError = (DictError) annotation;

            expectedCode = dictError.value();
        }
        
        if (e.getErrorCode() == expectedCode) {
            try {
                T instance = clazz.newInstance();
                
                if (e.getCause() != null) {
                    instance.initCause(e.getCause());
                }
                
                throw instance;
            }
            catch (InstantiationException | IllegalAccessException e1) {
                logger.error("Error while instancing exception " + clazz.getName(), e1);
            }
        }
    }
    
    private void propagateGeneralException(ServerException e) {
        propagateException(e, SyntaxErrorCommandException.class);
        propagateException(e, SyntaxErrorParametersException.class);
        propagateException(e, CommandNotImplementedException.class);
        propagateException(e, ParameterNotImplementedException.class);
        propagateException(e, ServerTemporaryNotAvailableException.class);
        propagateException(e, ServerShuttingDownException.class);
        propagateException(e, AccessDeniedException.class);
    }
}
