package core.context.api;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import core.context.TestContext;
import core.context.lifecycle.ContextBootstrap;
import core.utils.LogUtil;

public class Phase5AllTestsSuite implements BeforeAllCallback, AfterAllCallback  {
    private static TestContext testContext;

    /**
     * Initialize TestContext and ContextBootstrap before all tests in the suite.
     *
     * Store the initialized TestContext in the global store of the ExtensionContext.
     *
     * This is necessary because the tests in the suite need to access the TestContext
     * and also need to be able to clear it after the tests are finished.
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        LogUtil.info("Initializing Phase 5 – Api Context Implementation");

        // Initialize TestContext and ContextBootstrap
        testContext = ContextBootstrap.init();

        // Get the global store of the ExtensionContext
        ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);

        // Store the initialized TestContext in the global store of the ExtensionContext
        store.put("testContext", testContext);
    }

    /**
     * Clean up Phase 5 - Api Context Implementation after all tests in the suite are finished.
     *
     * This method is called after all tests in the suite are finished and is responsible for
     * clearing the TestContext.
     *
     * This is necessary because the TestContext is shared between all tests in the suite and
     * needs to be cleared after all tests are finished to avoid cross-test contamination.
     *
     * This method logs a message indicating that it is cleaning up Phase 5 and then clears the TestContext.
     * If the TestContext is null, it does not attempt to clear it.
     */
    @Override
    public void afterAll(ExtensionContext context) {
        LogUtil.info("Cleaning up Phase 5 – Api Context Implementation");

        // Get the global store of the ExtensionContext
        ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);

        // Clear the TestContext
        TestContext ctx = store.get("testContext", TestContext.class);
        if (ctx != null) ctx.clear();
    }

    /**
     * Retrieves the TestContext from the global store of the ExtensionContext.
     *
     * This method is useful for tests that need to access the TestContext.
     *
     * @param context the ExtensionContext
     * @return the TestContext stored in the global store of the ExtensionContext
     */
    public static TestContext getContext(ExtensionContext context) {
        return context.getRoot()
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .get("testContext", TestContext.class);
    }

    /**
     * Retrieves the TestContext that was initialized in the beforeAll method of the ExtensionContext.
     *
     * This method is useful for tests that need to access the TestContext in a static context.
     *
     * @return the TestContext initialized in the beforeAll method of the ExtensionContext
     */
    public static TestContext getContextStatic() {
        return testContext;
    }
}
