package core.context.api;

import core.context.ContextException;
import core.context.adapter.ResponseAdapter;

/**
 * Default implementation of ApiContext.
 *
 *  Responsibilities:
 * - Simple data holder (for debugging if needed)
 * - Implement storing responses and the adapter.
 * - Binds ResponseAdapter to ApiContext
 * - No validation logic included
 */
public class DefaultApiContext implements ApiContext {
    /**
     * Normalized response adapter.
     * Immutable after construction.
     */
    private final ResponseAdapter responseAdapter;

    /**
     * Constructor.
     *
     * @param responseAdapter normalized API response
     */
    public DefaultApiContext(ResponseAdapter responseAdapter) {
        // Fail fast if context is misconfigured
        if (responseAdapter == null) {
            throw new ContextException("Response Adapter must not be null");
        }

        // Store response adapter
        this.responseAdapter = responseAdapter;
    }

    /**
     * Get the normalized response adapter associated with this API context.
     * The ResponseAdapter object provides access to the HTTP status code, headers, and body.
     *
     * @return response adapter bound to this context
     */
    @Override
    public ResponseAdapter response() {
        return responseAdapter;
    }
}
