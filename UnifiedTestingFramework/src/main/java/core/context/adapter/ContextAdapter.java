package core.context.adapter;

import core.context.view.ContextView;

/**
 * Base interface for platform-agnostic context adapters.
 *
 * Responsibilities:
 * - Provide a standardized way to extract data from any context
 * - Convert tool-specific responses to framework-level views
 *
 * @param <T> type of the underlying raw response/tool
 */
public interface ContextAdapter<T> {

    /**
     * Converts a raw response to a ContextView instance.
     *
     * @param rawResponse the raw response from the tool (API response, Web element, etc.)
     * @return a ContextView instance
     */
    ContextView adapt(T rawResponse);
}