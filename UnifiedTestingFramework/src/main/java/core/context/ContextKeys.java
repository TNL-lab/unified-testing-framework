package core.context;

import java.time.Instant;

import core.enums.PlatformType;

/**
 * Root execution-level context keys.
 *
 * These keys are platform-agnostic and
 * valid throughout the entire test lifecycle.
 */
public final class ContextKeys {
	// Prevent instantiation
	private ContextKeys() {}
	
	// Unique identifier for the test execution
	public static final ContextKey<String> TEST_ID;

	// Platform under test (API, WEB, MOBILE, AI, ...)
	public static final ContextKey<PlatformType> PLATFORM;

	//Test start timestamp
	public static final ContextKey<Instant> START_TIME;

	static {
		TEST_ID = ContextKey.of("testId", String.class);
		PLATFORM = ContextKey.of("platform", PlatformType.class);
		START_TIME = ContextKey.of("startTime", Instant.class);
	}
}
