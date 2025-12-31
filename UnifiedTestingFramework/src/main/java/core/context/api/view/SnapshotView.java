package core.context.api.view;

/**
 * Snapshot-oriented API response view.
 *
 * Used for:
 * - Snapshot testing
 * - Regression comparison
 */
public interface SnapshotView extends ApiResponseView {
    // The snapshot of the API response
    String snapshot();
}
