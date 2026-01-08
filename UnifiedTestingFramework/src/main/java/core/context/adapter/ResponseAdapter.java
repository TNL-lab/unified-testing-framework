package core.context.adapter;

/**
 * Adapter interface for response-like objects.
 *
 * Defines a contract to normalize status, headers, and body regardless of the
 * underlying platform (API, Web, Mobile).
 *
 * This interface represents the adapter boundary between external tools
 * (RestAssured, OkHttp, Selenium, etc.) and framework-level views.
 *
 * Responsibilities:
 * - Marker interface for normalized responses
 * - Expose underlying raw object for advanced usage
 */

public interface ResponseAdapter extends ContextAdapter {
    /**
     * Returns the underlying raw response object
     */
    Object raw();
}