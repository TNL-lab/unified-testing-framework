package core.context.lifecycle;

import core.context.TestContext;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;
/**
 * Bootstrap class for initializing and managing the context system.
 *
 * Responsibilities:
 * - Initialize context registry (Context types , Context views)
 * - Provide access to TestContext
 * - Cleanup contexts after test execution
 */
public final class ContextBootstrap {

    // Prevent instantiation
    private ContextBootstrap() {}

    /**
     * Initialize context system for a test
     *
     * @return the initialized TestContext
     */
    public static TestContext init() {
        TestContext context = new TestContext();
        /*
         * NOTE:
         * Concrete Context types & Views
         * will be registered by higher layer (Phase 5+).
         *
         * Phase 3 only provides mechanism.
         */
        return context;
    }

    /**
     * Cleanup context system after test execution.
     *
     * Should be called in @AfterEach / @AfterMethod.
     */
    public static void cleanup(TestContext testContext) {
        if (testContext != null) {
            testContext.clear();
        }

        // Clear framework registries (test-scope)
        ContextRegistry.clear();
        ContextViewFactory.clear();
    }
}
