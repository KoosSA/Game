package client.logic;

import java.util.ArrayList;
import java.util.List;

import client.logic.internalEvents.IDisposeHandler;
import client.logic.internalEvents.IResizeHandler;
import client.logic.internalEvents.IUpdateHandler;


/**
 * Handles all logic events registered in the engine. This includes {@link IUpdateHandler Updates}, {@link IDisposeHandler Disposes} and {@link IResizeHandler ResizeEvents}.
 * @author Koos
 *
 */
public class InternalEventHandlerRegistry {
	
	private static List<IResizeHandler> resizeHandlers = new ArrayList<>();
	private static List<IDisposeHandler> disposalHandlers = new ArrayList<>();
	private static List<IUpdateHandler> updateHandlers = new ArrayList<>();

	/**
	 * Adds the resize event to the list to be executed on a window resize. <br>
	 * <b> ** Only call manually if you know what you are doing. **
	 * @param iResizeHandler
	 */
	public static void addResizeHandler(IResizeHandler iResizeHandler) {
		resizeHandlers.add(iResizeHandler);
	}
	
	/**
	 * This method is called once when the window resizes. It redirects the resize events to all of the resize subscribers.
	 * @param width
	 * @param height
	 */
	public static void onResize(int width, int height) {
		resizeHandlers.forEach(handler -> {
			handler.onResize(width, height);
		});
	}

	/**
	 * This method is called once when the application closes. It redirects the dispose events to all of the dispose subscribers.
	 */
	public static void dispose() {
		disposalHandlers.forEach(handler -> {
			handler.dispose();
		});
	}

	/**
	 * Adds a dispose subscriber to the internal list. Called through {@link IDisposeHandler#registerDisposeHandler() registerDisposeHandler()}. <br>
	 * <b> ** Only call manually if you know what you are doing. **
	 * @param iDisposeHandler
	 */
	public static void addDisposeHandler(IDisposeHandler iDisposeHandler) {
		disposalHandlers.add(iDisposeHandler);
	}

	/**
	 * Adds a update subscriber to the internal list. Called through {@link IUpdateHandler#registerUpdatable() registerUpdatable()}. <br>
	 * <b> ** Only call manually if you know what you are doing. **
	 * @param iUpdatable
	 */
	public static void addUpdatable(IUpdateHandler iUpdatable) {
		updateHandlers.add(iUpdatable);
	}
	
	/**
	 * This method is called once every update cycle. It calls the {@link IUpdateHandler#update(float) update(float delta)} method in all of the update subscribers.
	 */
	public static void update(float delta) {
		List<IUpdateHandler> toUpdate = new ArrayList<IUpdateHandler>(updateHandlers);
		toUpdate.forEach(handler -> {
			handler.update(delta);
		});
	}

	/**
	 * Sometimes the specific class must be paused / not updated. This method removes the event subscriber from the internal update registry.
	 * @param iUpdatable
	 */
	public static void removeUpdatable(IUpdateHandler iUpdatable) {
		updateHandlers.remove(iUpdatable);
	}

}
