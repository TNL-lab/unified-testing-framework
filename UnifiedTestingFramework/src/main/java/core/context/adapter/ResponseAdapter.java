package core.context.adapter;

import java.util.Map;

/**
 * Adapter interface for response-like objects.
 *
 * Defines a contract to normalize status, headers, and body regardless of the underlying platform (API, Web, Mobile).
 *
 * This interface represents the adapter boundary between external tools (RestAssured, OkHttp, Selenium, etc.) and framework-level views.
 */

public interface ResponseAdapter extends ContextAdapter {
    /**
     * Get HTTP or framework status code.
     */
    int statusCode();

    /**
     * Get a HTTP header or metadata by name.
     */
    String header();

    /**
     * Get response body as string.
     */
    String body();

    /**
     * Get response headers as a normalized map.
     */
    Map<String, String> headers();

    /**
     * Access underlying raw response object (optional).
     *
     * Used only by advanced integrations or debugging.
     */
    Object raw();
}