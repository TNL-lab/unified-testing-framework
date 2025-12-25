package core.context.adapter;

public interface ContextAdapter {
	/**
     * Check whether this adapter supports the given raw response.
     */
	boolean supports(Object rawResponse);
}
