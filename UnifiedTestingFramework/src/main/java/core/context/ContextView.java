package core.context;

/**
 * Marker interface for all context views.
 *
 * A ContextView represents a specific "view" or representation of the runtime validation context (e.g. Response, JSON, Snapshot).
 *
 * A ContextView:
 * - Is READ-ONLY
 * - Exposes a specific projection of context data
 */
public interface ContextView {
  
	/**
     * Indicates whether this view is available.
     */
	boolean isAvailable();
}
