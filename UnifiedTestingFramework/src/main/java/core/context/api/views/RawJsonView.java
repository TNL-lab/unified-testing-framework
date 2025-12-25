package core.context.api.views;

import core.context.ContextView;

/**
 * Provides access to raw JSON content.
 *
 * Used for:
 * - Schema validation
 * - Contract diff
 * - Field existence checks
 */
public interface RawJsonView extends ContextView {

	/**
	 * Get raw JSON string.
	 */
	String json();
}