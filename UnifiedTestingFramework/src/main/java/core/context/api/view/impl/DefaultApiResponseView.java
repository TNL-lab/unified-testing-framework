package core.context.api.view.impl;

import core.context.api.ApiContext;
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
    private final ApiContext context;

    /**
     * Construct a new read-only view from a ApiContext instance.
     *
     * @param context  the ApiContext instance
     */
    public DefaultApiResponseView(ApiContext context) {
        this.context = context;
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
