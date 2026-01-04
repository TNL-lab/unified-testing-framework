package core.context.api;

import core.context.adapter.ResponseAdapter;
import core.context.support.ContextPreconditions;

/**
 * Builder responsible for assembling ApiContext.
 *
 * Responsibilities:
 * - Build pattern for API context
 * - Set response adapter
 * - Validate required components
 * - Construct ApiContext in a controlled way
 *
 */
public class ApiContextBuilder {
    /**
     * Normalized response adapter.
     */
    private ResponseAdapter responseAdapter;

    /**
     * Bind response adapter to this builder.
     *
     * @param adapter normalized response adapter
     * @return builder itself (fluent API)
     */
    public ApiContextBuilder withResponseAdapter(ResponseAdapter adapter) {
        // Store response adapter
        this.responseAdapter = adapter;

        // Return builder
        return this;
    }

    /**
     * Builds the APIContext instance.
     *
     * @return fully initialized ApiContext
     * @throws ContextException/IllegalStateException if required components are missing
     */
    public ApiContext build() {
        // Fail fast if context is misconfigured
        ContextPreconditions.requireNonNull(responseAdapter, "ResponseAdapter must be provided before building ApiContext");

        //Return new DefaultApiContext
        return new DefaultApiContext(responseAdapter);
    }
}
