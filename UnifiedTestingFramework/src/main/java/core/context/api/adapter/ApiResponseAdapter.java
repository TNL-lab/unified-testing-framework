package core.context.api.adapter;

import java.util.Map;

import core.context.adapter.ResponseAdapter;

/**
 * API-level implementation of ResponseAdapter.
 *
 * Responsibilities:
 * - Represents a normalized HTTP response
 * - Decouples API context from HTTP client libraries
 */
public class ApiResponseAdapter implements ResponseAdapter {
    /** HTTP status code */
    private final int statusCode;

    /** Raw response body as string  */
    private final String body;

    /** HTTP headers */
    private final Map<String, String> headers;

    /**
     * Original raw response object (RestAssured / OkHttp).
     * Exposed ONLY for advanced debugging.
     */
    private final Object raw;

    public ApiResponseAdapter(int statusCode, Map<String, String> headers, String body, Object raw) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.raw = raw;
    }

    /**
     * Returns the HTTP status code of the API response.
     *
     * @return the HTTP status code
     */
    @Override
    public int statusCode() {
        return statusCode;
    }

    /**
     * Returns the raw response body as a string.
     *
     * @return the response body as a string
     */
    @Override
    public String body() {
        return body;
    }

    /**
     * Returns the HTTP headers as a normalized map.
     *
     * @return the HTTP headers as a map
     */
    @Override
    public Map<String, String> headers() {
        return headers;
    }

    /**
     * Returns the HTTP headers as a string representation.
     * @return the HTTP headers as a string
     */
    @Override
    public String header() {
        return headers.toString();
    }

    /**
     * Exposes the underlying raw response object from the HTTP client library (e.g. RestAssured, OkHttp).
     *
     * Intended for advanced debugging use cases only.
     *
     * @return the underlying raw response object
     */
    @Override
    public Object raw() {
        return raw;
    }
}
