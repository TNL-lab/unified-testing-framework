package core.context.api.view.impl;

import core.context.api.view.SnapshotView;

/**
 * Default read-only implementation of SnapshotView
 *
 * Responsibilities:
 * - Extend DefaultApiResponseView to reuse status/body/isSuccess
 * - Provide snapshot() method
 * - Immutable / read-only / assertion-friendly
 */
public class DefaultSnapshotView extends DefaultApiResponseView implements SnapshotView {

    /**
     * Construct a DefaultSnapshotView instance
     *
     * @param context  the DefaultApiContext instance
     *
     */
    public DefaultSnapshotView(Object context) {
        // Reuse context handling from DefaultApiResponseView
        super(context);
    }


    /**
     * Gets a snapshot of the API response in a string format.
     *
     * @return the snapshot of the API response as a string
     */
    @Override
    public String snapshot() {
        // Simply return the snapshot, or could do snapshot formatting later
        return "Status: " + super.statusCode() + ", Body: " + super.body();
    }
}
