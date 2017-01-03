/*
 * Dict4j, the OpenSource Java client for the DICT protocol 
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.dict4j;

/**
 * Dict protocol return code values
 * 
 * @author ROTH Damien
 */
public class DictReturnCode
{
    public static int N_DATABASES_PRESENT = 110;
    public static int N_STRATEGIES_AVAIBLES = 111;
    public static int DATABASE_INFORMATIONS = 112;
    public static int HELP_TEXT = 113;
    public static int SERVER_INFORMATIONS = 114;
    public static int CHALLENGE = 130; 
    public static int N_DEFINIIONS_RETRIEVED = 150;
    public static int WORD_DATABASE_NAME = 151;
    public static int N_MATCHES_FOUND = 152;
    public static int OPTIONAL_TIMING = 210;
    public static int CONNEXION_OK = 220;
    public static int CLOSING_CONNECTION = 221;
    public static int AUTHENTIFICATION_SUCCESSFUL = 230; 
    public static int OK = 250;
    public static int SEND_RESPONSE = 330;
    public static int SERVER_TEMPORARILY_UNAVAILABLE = 420; 
    public static int SERVER_DOWN_OP_REQUEST = 421;
    public static int SYNTAX_ERROR_COMMAND = 500;
    public static int SYNTAX_ERROR_ILLEGAL_PARAM = 501;
    public static int COMMAND_NOT_IMPLEMENTED = 502;
    public static int COMMAND_PARAM_NOT_IMPLEMENTED = 503;
    public static int ACCESS_DENIED = 530;
    public static int ACCESS_DENIED_GET_INFO = 531;
    public static int ACCESS_DENIED_UNKNOWN_MECHANISM = 532;
    public static int INVALID_DATABASE = 550;
    public static int INVALID_STRATEGY = 551;
    public static int NO_MATCH = 552;
    public static int NO_DATABASES_PRESENT = 554;
    public static int NO_STRATEGIES_AVAILABLE = 555;
    public static int JAVA_ERROR = 999;
}
