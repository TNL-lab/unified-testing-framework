package core.context.api.adapter;

import java.util.HashMap;
import java.util.Map;

import core.context.ContextException;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Adapter for OkHttp response.
 *
 * Responsibilities:
 * - Normalize OkHttp response
 * - Platform-specific logic for OkHttp
 * - Converts OkHttp Response into ApiResponseAdapter
 */
public final class OkHttpAdapter{
    //Constructor
    private OkHttpAdapter() {}

    /**
     * Adapt OkHttp response into normalized ApiResponseAdapter.
     *
     * @param response the response to adapt
     * @return the normalized ApiResponseAdapter
     */
    public static ApiResponseAdapter adapt(Response response){
        // Get the response status code
        int statusCode = response.code();

        // Get all headers from the response as a list
        Headers headersList = response.headers();

        // Convert the list of headers into a map
        Map<String, String> headers = new HashMap<>();

        // The map is used to store the headers in a normalized way
        headersList.toMultimap()
            .forEach((key, value) -> headers.put(key, String.join(",", value)));

        // Get the response body as a string
        String body = null;

        // Validate response body
        if (response.body() != null) {
            try {
                body = response.body().string();
            } catch (Exception e) {
                throw new ContextException("Error reading response body as string", e.getCause());
            }
        }

        // Create a new ApiResponseAdapter with the normalized headers
        return new ApiResponseAdapter(statusCode, headers, body, response);
    }
}
