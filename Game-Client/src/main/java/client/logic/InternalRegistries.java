package client.logic;

import java.util.ArrayList;
import java.util.List;

import client.logic.internalEvents.IDisposeHandler;
import client.logic.internalEvents.IResizeHandler;
import client.logic.internalEvents.IUpdatable;

public class InternalRegistries {
	
	private static List<IResizeHandler> resizeHandlers = new ArrayList<>();
	private static List<IDisposeHandler> disposalHandlers = new ArrayList<>();
	private static List<IUpdatable> updateHandlers = new ArrayList<>();

	public static void addResizeHandler(IResizeHandler iResizeHandler) {
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

	public static void addDisposeHandler(IDisposeHandler iDisposeHandler) {
		disposalHandlers.add(iDisposeHandler);
	}

	public static void addUpdatable(IUpdatable iUpdatable) {
		updateHandlers.add(iUpdatable);
	}
	
	public static void update(float delta) {
		updateHandlers.forEach(handler -> {
			handler.update(delta);
		});
	}

	public static void removeUpdatable(IUpdatable iUpdatable) {
		updateHandlers.remove(iUpdatable);
	}
	
	

}
