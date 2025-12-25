package core.context.registry;import org.w3c.dom.css.ViewCSS;

import core.context.ContextException;
import core.context.ContextView;
import core.context.ValidationContext;
import core.context.views.RawJsonView;
import core.context.views.ResponseView;
import core.context.views.SnapshotView;

/**
 * Factory responsible for creating ContextView instances.
 *
 * This class:
 * - Knows how to create each supported view
 * - Handles dependency between views
 * - Fails fast if a view is unsupported
 *
 * IMPORTANT:
 * - Validators MUST NOT create views directly
 * - All view creation goes through this factory
 */
public final class ContextViewFactory {
	private ContextViewFactory() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a context view of the given type.
	 *
	 * @param viewClass the view class to create
	 * @param context   the validation context
	 * @return a new context view instance
	 */
	public static ContextView createView(Class<? extends ContextView> viewClass, ValidationContext context) {

		if (viewClass.equals(ResponseView.class)) {
			return createResponseView(context);
		}

		if (viewClass.equals(SnapshotView.class)) {
			return createSnapshotView(context);
		}

		if (viewClass.equals(RawJsonView.class)) {
			return createRawJsonView(context);
		}

		throw new ContextException("Unsupported ContextView: " + viewClass.getSimpleName());
	}

	/**
	 * Creates a RawJsonView.
	 *
	 * RawJsonView depends on ResponseView.
	 */
	private static RawJsonView createRawJsonView(ValidationContext context) {
		ResponseView responseView = context.getViews(ResponseView.class);
		return new RawJsonView(responseView);
	}

	/**
	 * Creates a SnapshotView from the context source.
	 */
	private static SnapshotView createSnapshotView(ValidationContext context) {
		return new SnapshotView(context.getSource());
	}

	/**
	 * Creates a ResponseView from the context source.
	 */
	private static ResponseView createResponseView(ValidationContext context) {
		return new ResponseView(context.getSource());
	}
}
