package core.context.adapter;

import core.context.view.ContextView;

/**
 * Adapter interface for response-like objects.
 *
 * Provides a contract to normalize status, headers, and body
 * regardless of platform (API, Web, Mobile).
 */
public interface ResponseAdapter<T> extends ContextAdapter<T> {
    /**
     * Get HTTP or framework status code.
     */
    int statusCode(T rawResponse);

    /**
     * Get a HTTP header or metadata by name.
     */
    String header(T rawResponse, String headerName);

    /**
     * Get response body as string.
     */
    String body(T rawResponse);

    /**
     * Default implementation that wraps the raw response into a generic ContextView.
     *
     * @param rawResponse the raw response from the tool (API response, Web element, etc.)
     * @return a ContextView instance wrapping the raw response
     */
    @Override
    default ContextView adapt(T rawResponse) {
        // Default implementation could wrap response into a generic ContextView
        return new ResponseViewWrapper<>(this, rawResponse);
    }
}