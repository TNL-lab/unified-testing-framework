package api.enums;

/**
 * Defines supported API content types.
 * This enum represents API contract, NOT environment config.
 */
public enum ApiContentType {
   
    JSON("application/json"), 	 // JSON request/response
    XML("application/xml"), // XML request/response
    MULTIPART("multipart/form-data"),     //Multipart request
    FORM_URLENCODED("application/x-www-form-urlencoded");  // Form URL encoded request
    
    // Raw HTTP header value
    private final String value;
    
	/**
     * Enum constructor.
     *
     * @param value HTTP header value
     */
    ApiContentType(String value) {
        this.value = value;
    }
    
    /**
     * Get HTTP header value.
     */
    public String value() {
        return value;
    }

}
