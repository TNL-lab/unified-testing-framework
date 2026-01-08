package core.context.api;

import core.context.api.adapter.ApiResponseAdapter;
import core.context.support.ContextPreconditions;

/**
 * Default implementation of ApiContext.
 *
 * Responsibilities:
 * - Simple data holder (for debugging if needed)
 * - Implement storing responses and the adapter.
 * - No validation logic included
 */
public class DefaultApiContext implements ApiContext {
  /** Normalized response adapter. Immutable after construction. */
  private final ApiResponseAdapter responseAdapter;

  /**
   * Constructor.
   *
   * @param ApiResponseAdapter normalized API response
   */
  public DefaultApiContext(ApiResponseAdapter responseAdapter) {
    // Fail fast if context is misconfigured
    ContextPreconditions.requireNonNull(responseAdapter, "Api Response Adapter must not be null");

    // Store response adapter
    this.responseAdapter = responseAdapter;
  }

  /**
   * Get the normalized response adapter associated with this API context. The
   * ApiResponseAdapter
   * object provides access to the HTTP status code, headers, and body.
   *
   * @return response adapter bound to this context
   */
  @Override
  public ApiResponseAdapter response() {
    return responseAdapter;
  }
}
