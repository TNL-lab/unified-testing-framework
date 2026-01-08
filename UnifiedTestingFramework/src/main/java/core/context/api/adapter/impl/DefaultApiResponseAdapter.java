package core.context.api.adapter.impl;

import java.util.Map;

import core.context.api.adapter.ApiResponseAdapter;

/**
 * Default implementation of ApiResponseAdapter
 *
 * Responsibilities:
 * - Holds normalized HTTP response
 * - Decouples API context from HTTP client libraries
 * - Convert tool response (OkHttpAdapter / RestAssuredAdapter) ->
 * DefaultApiResponseAdapter
 */
public class DefaultApiResponseAdapter implements ApiResponseAdapter {
  /** HTTP status code */
  private final int statusCode;

  /** Raw response body as string */
  private final String body;

  /** HTTP headers */
  private final Map<String, String> headers;

  /** Original raw response object (RestAssured / OkHttp). */
  private final Object raw;

  /**
   * Constructor for DefaultApiResponseAdapter
   *
   * @param statusCode HTTP status code
   * @param headers    HTTP headers
   * @param body       Raw response body as string
   * @param raw        Original raw response object
   */
  public DefaultApiResponseAdapter(
      int statusCode, Map<String, String> headers, String body, Object raw) {
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
   * Exposes the underlying raw response object from the HTTP client library (e.g.
   * RestAssured,
   * OkHttp).
   *
   * @return the underlying raw response object
   */
  @Override
  public Object raw() {
    return raw;
  }
}
