package core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TestContextUsageTest {
    @Test
    void should_store_and_retrieve_typed_value() {
        // Create a new TestContext instance
        TestContext context = new TestContext();

        // Create a ContextKey for a String value
        ContextKey<String> contextKey = ContextKeyFactory.root("userId", String.class);

        // Store a value in the context
        context.put(contextKey, "user_123");

        // Retrieve the value from the context
        String value = context.get(contextKey);

        // Verify the retrieved value
        assertEquals("user_123", value);
        assertEquals("root.userId", contextKey.getName());
    }

    @Test
    void require_should_throw_when_missing() {
        // Create a new TestContext instance
        TestContext context = new TestContext();

        // Create a ContextKey for an Integer value
        ContextKey<Integer> contextKey = ContextKeyFactory.root("age", Integer.class);

        // Verify that require throws ContextException when the key is missing
        assertThrows(ContextException.class, () -> context.require(contextKey));
    }

    @Test
    void clear_should_remove_all_contexts() {
        // Create a new TestContext instance
        TestContext context = new TestContext();

        // Create a ContextKey for a String value
        ContextKey<String> contextKey = ContextKeyFactory.root("token", String.class);

        // Store a value in the context
        context.put(contextKey, "token_123");
        // Verify that the context is cleared
        assertEquals(true, context.contains(contextKey));
        
        // Clear the context
        context.clear();
        // Verify that the context is cleared
        assertThrows(ContextException.class, () -> context.require(contextKey));
    }
}
