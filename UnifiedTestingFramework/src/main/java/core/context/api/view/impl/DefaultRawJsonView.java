package core.context.api.view.impl;

import core.context.api.ApiContext;
import core.context.api.view.RawJsonView;

/**
 * Default read-only implementation of RawJsonView.
 *
 * Responsibilities:
 * - Extend DefaultApiResponseView to reuse status/body/isSuccess
 * - Provide rawJson() method
 * - Immutable / read-only / assertion-friendly
 *
 */
public class DefaultRawJsonView extends DefaultApiResponseView implements RawJsonView {

    /**
     * Construct a DefaultRawJsonView instance
     *
     * @param context  the ApiContext instance
     *
     */
    public DefaultRawJsonView(ApiContext context) {
        // Reuse context handling from DefaultApiResponseView
        super(context);
    }

    /**
     * Get the raw JSON string from context
     *
     * @return the raw JSON string
     */
    @Override
    public String json() {
        // Simply return the body, or could do JSON formatting later
        return super.body();
    }
}
