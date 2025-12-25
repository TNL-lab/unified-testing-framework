package core.context.api;

/**
 * Holds raw execution state of an API call.
 *
 * This class MUST NOT:
 * - Validate
 * - Parse business logic
 *
 * It only stores execution artifacts.
 */
public final class ApiContext {
    // Raw response object (tool-specific)
	private final Object rawResponse;
	
    // Raw response body as string (tool-specific)
	private final String rawBody;

    /**
     * Constructor.
     *
     * @param rawResponse tool-specific response (RestAssured, OkHttp, etc.)
     * @param rawBody     raw response body as string
     */
	public ApiContext(Object rawResponse, String rawBody) {
		this.rawResponse = rawResponse;
		this.rawBody = rawBody;
	}

    /**
     * Get raw response object.
     */
	public Object rawResponse() {
		return rawResponse;
	}

	/**
     * Get raw body object.
     */
	public String rawBody() {
		return rawBody;
	}
	
	
	
}
