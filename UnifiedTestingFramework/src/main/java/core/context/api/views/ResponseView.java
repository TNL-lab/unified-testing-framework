package core.context.api.views;

import core.context.ContextView;
import io.restassured.response.Response;

/**
 * Read-only view of an HTTP response.
 *
 * Validators MUST interact with this instead of raw response.
 */
public interface ResponseView extends ContextView {
	
    /**
     * Get HTTP status code.
     */
	int statusCode();
	
    /**
     * Get response header by name.
     */
    String header(String name);
    
    /**
     * Get response body as string.
     */
    String body();
}