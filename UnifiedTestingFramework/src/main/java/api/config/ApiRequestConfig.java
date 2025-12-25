package api.config;

import java.util.HashMap;
import java.util.Map;

import api.enums.ApiContentType;

/**
 * Configuration object for a single API request.
 *
 * Allows overriding default content-type, accept headers, baseUri, headers, auth,....
 * without affecting global behavior.
 */
public class ApiRequestConfig {
	// Request Content-Type (default: JSON)
	private ApiContentType contentType = ApiContentType.JSON;
	
	// Response Accept type (default: JSON)
	private ApiContentType accept = ApiContentType.JSON;
	
	private final Map<String, String> headers = new HashMap<>();

	 /**
     * Get request content type.
     */
	public ApiContentType getContentType() {
		return contentType;
	}

    /**
     * Override request content type.
     *
     * @param contentType desired content type
     * @return current config instance for chaining
     */
	public ApiRequestConfig withContentType(ApiContentType contentType) {
		this.contentType = contentType;
		return this;
	}
	
    /**
     * Get response accept type.
     */
	public ApiContentType getAccept() {
		return accept;
	}
	
    /**
     * Override response accept type.
     *
     * @param accept desired response type
     * @return current config instance for chaining
     */
	public ApiRequestConfig withAccept(ApiContentType acceptType) {
		this.accept = acceptType;
		return this;
	}
	
    /**
     * Add a custom header.
     *
     * @param name header name
     * @param value header value
     * @return current config instance
     */
	public ApiRequestConfig addHeader(String name, String value) {
		this.headers.put(name, value);
		return this;
	}
	
    /**
     * Get all custom headers.
     */
	public Map<String, String> getHeaders() {
		return headers;
	}
}