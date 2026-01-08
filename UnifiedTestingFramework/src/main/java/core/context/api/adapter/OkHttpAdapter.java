package core.context.api.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import core.context.ContextException;
import core.context.api.adapter.impl.DefaultApiResponseAdapter;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Adapter for OkHttp response.
 *
 * Responsibilities:
 * - Normalize OkHttp response
 * - Platform-specific logic for OkHttp
 * - Converts OkHttp Response into DefaultApiResponseAdapter
 */
public final class OkHttpAdapter {
  // Constructor
  private OkHttpAdapter() {
  }

  /**
   * Adapt OkHttp response into normalized DefaultApiResponseAdapter.
   *
   * @param response the response to adapt
   * @return the normalized DefaultApiResponseAdapter
   */
  public static ApiResponseAdapter adapt(Response response) {
    // Get the response status code
    int statusCode = response.code();

    // Get all headers from the response as a list
    Headers headersList = response.headers();

    // Convert the list of headers into a map
    Map<String, String> headers = new HashMap<>();

    // The map is used to store the headers in a normalized way
    headersList.toMultimap().forEach((key, value) -> headers.put(key, String.join(",", value)));

    // Get the response body as a string
    String body = Optional.ofNullable(response.body())
        .map(
            responseBody -> {
              try {
                return responseBody.string();
              } catch (Exception e) {
                throw new ContextException("Error reading response body as string", e);
              }
            })
        .orElse(null);

    // Create a new DefaultApiResponseAdapter with the normalized headers
    return new DefaultApiResponseAdapter(statusCode, headers, body, response);
  }
}
