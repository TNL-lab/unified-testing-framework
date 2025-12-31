package core.context.api;

import core.context.adapter.ResponseAdapter;

/**
 * API-specific context contract.
 *
 * Responsibilities:
 * - Represents an API execution context in a test
 * - Exposes API-related data in a normalized way
 */
public interface ApiContext {
    /**
     * Get the ResponseAdapter instance associated with this API context.
     * The ResponseAdapter holds the HTTP response body and headers.
     *
     * @return the ResponseAdapter normalized API response adapter (tool-agnostic, read-only)t
     */
    ResponseAdapter response();
}