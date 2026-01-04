package core.utils;

import java.util.function.Supplier;

/**
 * Utility class for performing precondition checks (fail-fast).
 */
public final class PreconditionUtil {

    //Constructor
    private PreconditionUtil() {}

    /**
     * Requires the given value to be non-null.
     *
     * Example usage:
     * String value = PreconditionUtil.requireNonNull(obj,
     *                    () -> new IllegalArgumentException("Object must not be null"));
     *
     * @param value the value to check
     * @param exceptionSupplier supplies the exception to throw if value is null
     * @return the value if not null
     */
    public static <T> T requireNonNull(
        T value,
        Supplier<? extends RuntimeException> exception) {
        // Check that the value is not null
        if (value == null) {
            throw exception.get();
        }
        // Return the value
        return value;
    }

    /**
     * Requires the given condition to be true.
     *
     * Example usage:
     * PreconditionUtil.requireTrue(value.length() > 0,
     *                    () -> new IllegalStateException("String must not be empty"));
     *
     * @param condition           the condition to check
     * @param exceptionSupplier   the supplies the exception to throw if condition is false
     */
    public static void requireTrue(
        boolean condition,
        Supplier<? extends RuntimeException> exception) {
        // Check that the condition is true
        if (!condition) {
            throw exception.get();
        }
    }
}
