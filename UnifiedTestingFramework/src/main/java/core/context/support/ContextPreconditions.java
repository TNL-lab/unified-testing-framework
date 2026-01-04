package core.context.support;

import core.context.ContextException;
import core.utils.PreconditionUtil;

/**
 * Context-specific preconditions.
 *
 * This class provides convenient fail-fast checks for the Context layer.
 */
public final class ContextPreconditions {

    // Prevent instantiation
    private ContextPreconditions() {
    }

    /**
     * Requires the given value to be non-null.
     *
     * @param value   the value to check
     * @param message the error message used when value is null
     * @param <T>     the value type
     * @return the value if not null
     * @throws ContextException    if value is null
     */
    public static <T> T requireNonNull(T value, String message) {
        // Check that the value is not null
        return PreconditionUtil.requireNonNull(
            value,
            () -> new ContextException(message)
        );
    }

    /**
     * Requires the given condition to be true.
     *
     * @param condition the condition to check
     * @param message   the error message used when condition is false
     * @throws ContextException if condition is false
     */
    public static void requireTrue(boolean condition, String message) {
        // Check that the condition is true
        PreconditionUtil.requireTrue(
            condition,
            () -> new ContextException(message)
        );
    }
}
