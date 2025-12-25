package core.context.adapter;

import core.context.views.ResponseView;

/**
 * Adapter responsible for converting a raw response into a tool-agnostic ResponseView.
 */
public interface ResponseAdapter {
	

	
    /**
     * Convert raw response into ResponseView.
     */
	ResponseView raw(Object rawResponse, String rawBody);
}
