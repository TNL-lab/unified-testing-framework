package core.context.api;

import core.context.api.adapter.ApiResponseAdapter;

/**
 * API-specific context contract.
 *
 * Responsibilities:
 * - Represents an API execution context in a test
 * - Exposes API-related data in a normalized way
 */
public interface ApiContext {
  /**
   * Get the API Response Adapter instance associated with this API context. The
   * ApiResponseAdapter
   * holds the HTTP response body and headers.
   *
   * @return the ApiResponseAdapter normalized API response adapter
   *         (tool-agnostic, read-only)
   */
  ApiResponseAdapter response();
}
