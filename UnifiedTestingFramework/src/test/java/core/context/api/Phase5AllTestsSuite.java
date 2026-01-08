package core.context.api;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import core.context.TestContext;
import core.context.lifecycle.ContextBootstrap;
import core.utils.LogUtil;
import io.restassured.builder.ResponseBuilder;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Phase5AllTestsSuite
        implements BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static TestContext testContext;

    /**
     * Initializes the Phase 5 – Api Context Implementation.
     *
     * This method is called once before all tests in the suite are run.
     *
     * It is responsible for initializing the TestContext and ContextBootstrap, and
     * storing the initialized TestContext in the global store of the
     * ExtensionContext.
     *
     * If the current test is the root node (i.e. the suite), the Phase 5 is
     * initialized.
     * Otherwise, the Phase 5 is ended and the TestContext is cleared.
     *
     * @param context the ExtensionContext
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        if (isRoot(context)) {
            LogUtil.info("Initializing Phase 5 – Api Context Implementation");

            /*
             * =====================================================
             * (0) BOOTSTRAP / REGISTRATION PHASE
             * =====================================================
             */

            // Initialize TestContext and ContextBootstrap
            testContext = ContextBootstrap.init();

            // Get the global store of the ExtensionContext
            ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);

            // Store the initialized TestContext in the global store of the ExtensionContext
            store.put("testContext", testContext);
        } else {
            LogUtil.info("Starting Phase 5 - Test for " + context.getRequiredTestClass().getSimpleName());
        }

    }

    /**
     * Clean up Phase 5 - Api Context Implementation after all tests in the suite
     * are finished.
     *
     * This method is called after all tests in the suite are finished and is
     * responsible for
     * clearing the TestContext.
     *
     * This is necessary because the TestContext is shared between all tests in the
     * suite and
     * needs to be cleared after all tests are finished to avoid cross-test
     * contamination.
     *
     * This method logs a message indicating that it is cleaning up Phase 5 and then
     * clears the TestContext.
     * If the TestContext is null, it does not attempt to clear it.
     */
    @Override
    public void afterAll(ExtensionContext context) {
        if (isRoot(context)) {
            LogUtil.info("Cleaning up Phase 5 – Api Context Implementation");

            // Get the global store of the ExtensionContext
            ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);

            // Clear the TestContext
            TestContext ctx = store.get("testContext", TestContext.class);
            if (ctx != null)
                ctx.clear();
        } else {
            LogUtil.info("Ending Phase 5 - Test for " + context.getRequiredTestClass().getSimpleName());
        }

    }

    /**
     * Returns true if the given ExtensionContext is the root node of the test
     * execution, i.e. the suite. Otherwise, returns false.
     * This method is used to determine if the current test is the root node
     * of the test execution.
     *
     * @param context the ExtensionContext
     * @return true if the current test is the root node of the test execution,
     *         false otherwise
     */
    private boolean isRoot(ExtensionContext context) {
        return context.getParent().isEmpty(); // root node = suite
    }

    /**
     * Called before the execution of each test in the suite.
     *
     * This method logs a message indicating the start of a test.
     *
     * @param context the ExtensionContext
     */
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String testName = context.getRequiredTestMethod().getName();
        LogUtil.info("Starting test: " + testName);
    }

    /**
     * Called after the execution of each test in the suite.
     *
     * Logs a message indicating the end of a test.
     *
     * @param context the ExtensionContext
     */
    @Override
    public void afterTestExecution(ExtensionContext context) {
        String testName = context.getRequiredTestMethod().getName();
        LogUtil.info("Ending test: " + testName);
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
     * Retrieves the TestContext that was initialized in the beforeAll method of the
     * ExtensionContext.
     *
     * This method is useful for tests that need to access the TestContext in a
     * static context.
     *
     * @return the TestContext initialized in the beforeAll method of the
     *         ExtensionContext
     */
    public static TestContext getContextStatic() {
        if (testContext == null) {
            testContext = ContextBootstrap.init();
        }
        return testContext;
    }

    /**
     * Builds an OkHttp Response object with the given url, code, headers and body.
     *
     * @param url     the URL of the OkHttp request
     * @param code    the status code of the Okhttp response
     * @param headers the headers of the Okhttp response
     * @param body    the body of the Okhttp response
     * @return an Okhttp Response object
     */
    public static Response buildOkHttpResponse(String url, int code, Headers headers, ResponseBody body) {
        // OkHttp Request
        Request request = new Request.Builder()
                .url(url)
                .build();

        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("OK")
                .headers(headers)
                .body(body)
                .build();
    }

    /**
     * Builds a RestAssured Response object with the given status code, body and
     * headers.
     *
     * This method is useful for tests that need to create a RestAssured Response
     * object
     * in order to verify the behavior of the application under test.
     *
     * @param statusCode the status code of the response
     * @param body       the body of the response
     * @param headers    the headers of the response
     * @return a RestAssured Response object with the given status code, body and
     *         headers
     */
    public static io.restassured.response.Response buildRestAssuredResponse(
            int statusCode,
            String body,
            io.restassured.http.Headers headers) {
        ResponseBuilder builder = new ResponseBuilder();

        builder.setStatusCode(statusCode);
        builder.setBody(body);

        builder.setHeaders(headers);

        return builder.build();
    }
}
