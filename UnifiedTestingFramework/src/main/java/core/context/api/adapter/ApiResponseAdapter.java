package core.context.api.adapter;

import java.util.Map;

import core.context.adapter.ResponseAdapter;

/**
 * API-level adapter interface
 *
 * Responsibilities:
 * - Represents a normalized HTTP response
 * - Provides HTTP-specific methods: status code, headers, body
 */
public interface ApiResponseAdapter extends ResponseAdapter {
  /** HTTP status code */
  int statusCode();

  /** Raw response body as string */
  String body();

  /** HTTP headers */
  Map<String, String> headers();
}
