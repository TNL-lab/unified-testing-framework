package api.enums;

/**
 * Centralized HTTP status codes
 */
public enum HttpStatus {
	
	//2xx: Success
	OK(200),
	CREATED(201),
	NO_CONTENT(204), 
	
	//3xx: Redirection
	NOT_MODIFIED(304),
	
	//4xx: Client error
	BAD_REQUEST(400),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	NOT_FOUNR(404),
	TOO_MANY_REQUEST(429),
	
	//5xx: Sever error
	INTERNAL_SERVER_ERROR(500),
	SERVICE_UNAVAILABLE(503)
	;
	
	private final int code;
	
	HttpStatus(int code) {
		this.code = code;
	}
	
	public int code() {
		return code;
	}
}
