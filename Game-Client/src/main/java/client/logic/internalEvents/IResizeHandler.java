package client.logic.internalEvents;

import client.logic.InternalRegistries;

public interface IResizeHandler {
	
	default void registerResizeHandler() {
		InternalRegistries.addResizeHandler(this);
	}
	
	void onResize(int width, int height);

}
