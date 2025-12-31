package core.context.api.view;

import core.context.view.ContextView;

/**
 * Read-only view for API responses.
 *
 * Responsibilities:
 * - Assertion-friendly interface
 * - Decoupled from HTTP client
 * - Immutable / read-only
 * - Hide tools and adapter
 */
public interface ApiResponseView extends ContextView {

    /**
     * Get HTTP or framework status code.
     *
     * @return the status code for the API response
     */
    int statusCode();

     /**
     * Get the response body as a string.
     *
     */
    String body();

    /**
     * Convenience method for common assertions.
     */
    boolean isSuccess();
}
