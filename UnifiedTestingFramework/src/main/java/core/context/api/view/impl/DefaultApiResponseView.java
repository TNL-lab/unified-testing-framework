package core.context.api.view.impl;

import core.context.ContextException;
import core.context.api.DefaultApiContext;
import core.context.api.view.ApiResponseView;

/**
 * Default read-only implementation of ApiResponseView.
 *
 * Responsibilities:
 * - Provide immutable, assertion-friendly access to HTTP response data
 * - Expose status code, body, and success flag
 * - Decouple test assertions from underlying HTTP client (RestAssured, OkHttp, etc.)
 * - Serve as the default concrete view for DefaultApiContext
 *
 */
public class DefaultApiResponseView implements ApiResponseView {

    // Hold reference to context instance to read data
    private final DefaultApiContext context;

    /**
     * Construct a new read-only view from a DefaultApiContext instance.
     *
     * @param context  the DefaultApiContext instance
     * @throws ContextException if context is not a DefaultApiContext
     */
    public DefaultApiResponseView(Object context) {
        // Fail fast if context is misconfigured
        if (!(context instanceof DefaultApiContext)) {
            throw new ContextException("Context is expected to be of type DefaultApiContext");
        }

        // Context is expected to be DefaultApiContext
        this.context = (DefaultApiContext) context;
    }

    /**
     * Get the HTTP status code from context
     *
     * @return the HTTP status code
     */
    @Override
    public int statusCode() {
         // Return HTTP status code from context
        return context.response().statusCode();
    }

    /** Gets the HTTP response body from context
     *
     * @return the HTTP response body from context
     */
   @Override
    public String body() {
         // Return response body from context
        return context.response().body();
    }

    /**
     * Checks if the HTTP status code is a success
     *
     * @return true if the HTTP status code is a success, false otherwise
     */
    @Override
    public boolean isSuccess() {
        // Get the HTTP status code
        int code = statusCode();

        // Return true if the HTTP status code is a success
        return code >= 200 && code < 300;
    }
}
