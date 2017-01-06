package net.java.dict4j.data;

public enum ServerCapability {
    /**
     * The OPTION MIME command is supported
     */
    OPTION_MIME("mime"),
    
    /**
     * The AUTH command is supported
     */
    BASIC_AUTHENTICATION("auth"),
    
    /**
     * The SASL Kerberos version 4 mechanism is supported
     */
    SASL_KERBEROS_V4("kerberos_v4"),
    
    /**
     * The SASL GSSAPI [RFC2078] mechanism is supported
     */
    SASL_GSSAPI("gssapi"),
    
    /**
     * The SASL S/Key [RFC1760] mechanism is supported
     */
    SASL_SKEY("skey"),
    
    /**
     * The SASL external mechanism is supported
     */
    SASL_EXTERNAL("external"),
    
    /**
     * Unknown capability, see log to discover which one
     */
    UNKNOWN(null);
    
    private final String code;
    
    private ServerCapability(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public static ServerCapability getFromCode(String code) {
        for (ServerCapability sc : values()) {
            if (sc.getCode() != null && sc.getCode().equals(code)) {
                return sc;
            }
        }
        
        return UNKNOWN;
    }
}
