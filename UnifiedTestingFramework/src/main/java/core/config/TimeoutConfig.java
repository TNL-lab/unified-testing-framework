package core.config;

import java.time.Duration;

/**
 * Centralized timeout configuration for the framework.
 * All timeout values should be defined here.
 */
public final class TimeoutConfig {
	// Prevent instantiation
	public TimeoutConfig() {}	
	
	//=============WEB=================
	//Implicit wait timeout.
	public static Duration implicitWait() {
		return ConfigParser.getDuration("timeout.implicit");
	}
	
	//Page load timeout.
	public static Duration pageLoad() {
		return ConfigParser.getDuration("timeout.pageLoad");
	}
	
	//Explicit wait timeout.
	public static Duration explicitWait() {
		return ConfigParser.getDuration("timeout.explicit");
	}
	
	//Fluent wait total timeout.
	public static Duration fluentTimeout() {
		return ConfigParser.getDuration("timeout.fluent.total");
	}
	
	//Fluent wait polling interval.
	public static Duration fluentPolling() {
		return ConfigParser.getDuration("timeout.fluent.polling");
	}
	
	//=============API=================
	//Api timeout.
	public static Duration apiTimeout() {
		return ConfigParser.getDuration("api.timeout");
	}
	
	
}
