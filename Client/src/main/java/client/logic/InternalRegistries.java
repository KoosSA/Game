package client.logic;

import java.util.ArrayList;
import java.util.List;

import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IResizable;
import client.logic.internalEvents.IUpdatable;


/**
 * Handles all logic events registered in the engine. This includes {@link IUpdatable Updatables} and {@link IDisposable Disposables}.
 * @author Koos
 *
 */
public class InternalRegistries {
	
	private static List<IResizable> resizeHandlers = new ArrayList<>();
	private static List<IDisposable> disposalHandlers = new ArrayList<>();
	private static List<IUpdatable> updateHandlers = new ArrayList<>();

	public static void addResizeHandler(IResizable iResizeHandler) {
		resizeHandlers.add(iResizeHandler);
	}
	
	public static void onResize(int width, int height) {
		resizeHandlers.forEach(handler -> {
			handler.onResize(width, height);
		});
	}

	public static void dispose() {
		disposalHandlers.forEach(handler -> {
			handler.dispose();
		});
	}

	public static void addDisposeHandler(IDisposable iDisposeHandler) {
		disposalHandlers.add(iDisposeHandler);
	}

	public static void addUpdatable(IUpdatable iUpdatable) {
		updateHandlers.add(iUpdatable);
	}
	
	public static void update(float delta) {
		List<IUpdatable> toUpdate = new ArrayList<IUpdatable>(updateHandlers);
		toUpdate.forEach(handler -> {
			handler.update(delta);
		});
	}

	public static void removeUpdatable(IUpdatable iUpdatable) {
		updateHandlers.remove(iUpdatable);
	}
	
	

}
