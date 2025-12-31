package core.context.api.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.restassured.http.Header;
import io.restassured.response.Response;

/**
 * Adapter for RestAssured response.
 *
 * Responsibilities:
 * - Normalize RestAssured response
 * - Platform-specific logic for RestAssured
 * - Converts RestAssured Response into ApiResponseAdapter
 */
public final class RestAssuredAdapter {
    // Private constructor
    private RestAssuredAdapter() {}

    /**
     * Adapt RestAssured response into normalized ApiResponseAdapter.
     *
     * @param response the response to adapt
     * @return the normalized ApiResponseAdapter
     */
    public static ApiResponseAdapter adapt(Response response) {
        // Get the response status code
        int statusCode = response.getStatusCode();

        // Get all headers from the response as a list
        List<Header> headersList = response.getHeaders().asList();

        // Convert the list of headers into a map
        // The map is used to store the headers in a normalized way
        Map<String, String> headers = headersList.stream()
                .collect(Collectors.toMap(
                        Header::getName, // Use the header name as the key
                        Header::getValue, // Use the header value as the value
                        (v1, v2) -> v1) // If there are multiple headers with the same name, use the first one
                        );

        // Get the response body as a string
        String body = response.getBody().asString();

        // Create a new ApiResponseAdapter with the normalized headers
        return new ApiResponseAdapter( statusCode, headers, body, response);
    }
}
