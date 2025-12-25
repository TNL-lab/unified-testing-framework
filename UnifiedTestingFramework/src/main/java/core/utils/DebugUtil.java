package core.utils;

public final class DebugUtil {
	private DebugUtil() {
	}
	
    /**
     * Pause execution for debug purpose ONLY
     */
	 public static void pause(int second) {
		if (second <= 0) {
			return;
		}
		try {
			Thread.sleep(second * 1000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
