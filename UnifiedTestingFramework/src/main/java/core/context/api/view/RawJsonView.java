package core.context.api.view;

/**
 * Specialized API view for raw JSON access.
 *
 * Notes:
 * - Does NOT parse JSON
 * - Parsing responsibility belongs to validator/assertion layer
 */
public interface RawJsonView extends ApiResponseView {
    // The raw JSON string
    String json();
}
