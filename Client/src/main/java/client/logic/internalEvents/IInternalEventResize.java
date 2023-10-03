package client.logic.internalEvents;

import client.logic.InternalEventRegistry;

public interface IInternalEventResize {
	
	default void registerResizeHandler() {
		InternalEventRegistry.addResizeHandler(this);
	}
	
	void onResize(int width, int height);

}
