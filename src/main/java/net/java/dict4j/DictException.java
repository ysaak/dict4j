/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * Exception class managing the basics dict server errors.
 * 
 * @author LITZELMANN Cedric
 * @author ROTH Damien
 */
public class DictException
    extends Exception
{
    /**
     * The error code returned bu the server
     */
    private int code;
    
    /**
     * The message explaining the error
     */
    private String message = null;
    
    /**
     * The java exception thown
     */
    private Exception exception;
    
    /**
     * Create an exception from the dict error code
     * @param error Error code returned by the server
     */
    public DictException(int errorCode)
    {
        this.code = errorCode;
    }
    
    /**
     * Same as the first constructor but with a string (converted to an int)
     * @param error Error code returned by the server
     */
    public DictException(String error)
    {
        this.code = Integer.parseInt(error);
    }
    
    /**
     * Create an exception with a custom message
     * @param error Error code returned by the server
     * @param message Custom message
     */
    public DictException(int errorCode, String message)
    {
        this.code = errorCode;
        this.message = message;
    }
    
    /**
     * Create an exception from a java exception
     * @param e Java Exception
     */
    public DictException(Exception e)
    {
        this.code = DictReturnCode.JAVA_ERROR;
        this.message = e.toString();
        this.exception = e;
    }

    /**
     * Return the error code
     * @return the error code
     */
    public int getErrorCode()
    {
        return this.code;
    }
    
    /**
     * Return the error message
     * @return the error message
     */
    public String getMessage()
    {
        if (this.message == null)
        {
            this.message = generateMessage();
        }
        return this.message;
    }
    
    /**
     * Returns the java exception that might be thrown
     * @return the java exception that might be thrown
     */
    public Exception getException()
    {
        return this.exception;
    }
    
    /**
     * Get an explanation of the error
     * @return  Returns an explanation corresponding to the current error code
     */
    private String generateMessage()
    {
        String result;
        switch(this.code)
        {
        case 110 :
            result = "n databases present";
            break;
        case 111 :
            result = "n strategies available";
            break;
        case 112 :
            result = "database information follows";
            break;
        case 113 :
            result = "help text follows";
            break;
        case 114 :
            result = "server information follows";
            break;
        case 130 :
            result = "challenge follows";
            break;
        case 150 :
            result = "n definitions retrieved";
            break;
        case 151 :
            result = "word database name";
            break;
        case 152 :
            result = "n matches found";
            break;
        case 210 :
            result = "optional timing";
            break;
        case 220 :
            result = "Connexion OK";
            break;
        case 221 :
            result = "Closing Connection";
            break;
        case 230 :
            result = "Authentification successful";
            break;
        case 250 :
            result = "Ok";
            break;
        case 330 :
            result = "send response";
            break;
        case 420 :
            result = "Server temporarily unavailable";
            break;
        case 421 :
            result = "Server shutting down at operator request";
            break;
        case 500 :
            result = "Syntax error, command not recognized";
            break;
        case 501 :
            result = "Syntax error, illegal parameters";
            break;
        case 502 :
            result = "Command not implemented";
            break;
        case 503 :
            result = "Command parameter not implemented";
            break;
        case 530 :
            result = "Access denied";
            break;
        case 531 :
            result = "Access denied, use SHOW INFO for server information";
            break;
        case 532 :
            result = "Access denied, unknown mechanism";
            break;
        case 550 :
            result = "Invalid database, use SHOW DB for list of databases";
            break;
        case 551 :
            result = "Invalid strategy, use(this.error). SHOW STRAT for a list of strategies";
            break;
        case 552 :
            result = "No match";
            break;
        case 554 :
            result = "No databases present";
            break;
        case 555 :
            result = "No strategies available";
            break;
        default:
            if (code >= DictReturnCode.JAVA_ERROR)
            {
                result = this.message;
            }
            else
            {
                result = this.errorGen(code);
            }
        }
        
        return result;
    }
    
    /**
     * Get informations about unknowns errors
     * @param err Error number
     * @return Error definition
     */
    private String errorGen(int err)
    {
        String error_type = Integer.toString(err);
        
        String result = new String();
        
        if(error_type.startsWith("1"))
        {// test on digit one
            result = "Positive Preliminary reply : ";
        }
        else if(error_type.startsWith("2"))
        {
            result = "Positive Completion reply : " ;
        }
        else if(error_type.startsWith("3"))
        {
            result =  "Positive Intermediate reply : ";
        }
        else if(error_type.startsWith("4"))
        {
            result = "Transient Negative Completion reply : ";
        }
        else if(error_type.startsWith("5"))
        {
            result = "Permanent Negative Completion reply : ";
        }
        else
        {
            return "Unknown error";
        }
        
        //test on digit two
        if(error_type.charAt(1) == '0')
        {
            result += "Syntax";
        }
        else if ( error_type.charAt(1) == '1')
        {
            result += "Information";
        }
        else if ( error_type.charAt(1) == '2')
        {
            result += "Connections";
        }
        else if ( error_type.charAt(1) == '3')
        {
            result += "Authentication";
        }
        else if ( error_type.charAt(1) == '4')
        {
            result += "Unspecified as yet";
        }
        else if ( error_type.charAt(1) == '5')
        {
            result += "DICT System";
        }
        else if ( error_type.charAt(1) == '8')
        {
            result += "Nonstandard (private implementation) extensions";
        }
        
        return result;
    }
}
