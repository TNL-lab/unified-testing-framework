package core.context.view;

/**
 * Base interface for read-only views of context data.
 *
 * Responsibilities:
 * - Expose data in a test-friendly, assertable format
 * - Immutable / read-only
 */
public interface ContextView {
    /**
     * Optionally, return a descriptive summary of this view.
     *
     * @return a concise string summary of the context view
     */
    default String summary() {
        // Default implementation returns toString
        return this.toString();
    }
}
