package core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.context.api.ApiContext;
import core.context.api.view.impl.DefaultApiResponseView;
import core.context.lifecycle.ContextBootstrap;
import core.context.registry.ContextRegistry;
import core.context.registry.ContextViewFactory;
import core.context.view.ContextView;
import core.utils.LogUtil;

public class Phase3ContextTest {

  // Dummy classes for testing
  private static class DummyContext implements ContextView {
  };

  private static class UnregisteredContext implements ContextView {
  }

  @BeforeEach
  public void setUp() {
    LogUtil.info("Initializing Phase 3 – Lifecycle + Registry");
    // Clear Context Registry before each test to avoid cross-test contamination
    ContextRegistry.clear();

    // Clear Context Views before each test to avoid cross-test contamination
    ContextViewFactory.clear();
  }

  @AfterEach
  public void tearDown() {
    LogUtil.info("Cleaning up  Phase 3 – Lifecycle + Registry");
    // Clear Context Registry after each test
    ContextRegistry.clear();

    // Clear Context Views after each test
    ContextViewFactory.clear();
  }

  @Test
  void test_registry_register_and_keyOf() {
    // Register DummyContext with ContextRegistry
    ContextRegistry.register(DummyContext.class, ContextNamespace.API);

    // Verify that DummyContext is registered
    assertTrue(ContextRegistry.isRegistered(DummyContext.class));

    // Get ContextKey for DummyContext
    ContextKey<DummyContext> key = ContextRegistry.keyOf(DummyContext.class);

    // Verify ContextKey not null
    assertNotNull(key);

    // Verify ContextType of ContextKey
    assertEquals(DummyContext.class, key.getType());

    // Verify Name of ContextKey
    assertTrue(key.getName().contains("dummyContext"));
  }

  @Test
  void test_registry_keyOf_Unregistered() {
    // Attempt to get ContextKey for unregistered context type
    Exception exception = assertThrows(
        ContextException.class,
        () -> {
          ContextRegistry.keyOf(UnregisteredContext.class);
        });

    // Verify exception message
    assertTrue(exception.getMessage().contains("Unregistered context type"));
  }

  @Test
  void test_ContextViewFactory_Resolve() {
    // Register DummyContext with ContextRegistry
    ContextRegistry.register(DummyContext.class, ContextNamespace.API);

    // Register a dummy view supplier for DummyContext
    DummyContext dummyInstance = new DummyContext();
    ContextViewFactory.register(
        DummyContext.class, ctx -> new DefaultApiResponseView((ApiContext) ctx));

    // Create ContextView for DummyContext
    ContextView view = ContextViewFactory.createView(DummyContext.class);

    // Verify that the created view is not null
    assertNotNull(view);

    // Verify that the created view is the same as the dummy instance
    assertEquals(dummyInstance, view);
  }

  @Test
  void test_ContextViewFactory_Resolve_Unregistered() {
    // Attempt to create ContextView for unregistered context type
    Exception exception = assertThrows(
        ContextException.class, () -> ContextViewFactory.createView(UnregisteredContext.class));

    // Verify exception message
    assertTrue(exception.getMessage().contains("No view registered"));
  }

  @Test
  void test_ContextBootstrap_Init_And_Cleanup() {
    // Before init registry empty
    assertFalse(ContextRegistry.isRegistered(DummyContext.class));

    // Initialize context system
    ContextBootstrap.init();

    // Register DummyContext
    ContextRegistry.register(DummyContext.class, ContextNamespace.ROOT);

    // After init, contexts should be registered
    assertTrue(ContextRegistry.isRegistered(DummyContext.class));

    // Cleanup context system
    ContextBootstrap.cleanup(new TestContext());

    // After cleanup, registry should be empty
    assertFalse(ContextRegistry.isRegistered(DummyContext.class));
  }
}
