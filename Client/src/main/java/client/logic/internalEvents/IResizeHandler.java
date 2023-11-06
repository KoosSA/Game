package client.logic.internalEvents;

import client.logic.InternalEventHandlerRegistry;

public interface IResizeHandler {
	
	default void registerResizeHandler() {
		InternalEventHandlerRegistry.addResizeHandler(this);
	}
	
	void onResize(int width, int height);

}
