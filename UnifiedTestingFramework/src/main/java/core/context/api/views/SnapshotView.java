package core.context.api.views;

import java.util.Map;

import core.context.ContextView;

/**
 * Snapshot view of parsed contract data.
 *
 * Used for strict contract validation and diffing.
 */
public interface SnapshotView extends ContextView{
	
	/**
     * Get snapshot fields mapped as (path -> type).
     */
	Map<String, Class<?>> fields();
}