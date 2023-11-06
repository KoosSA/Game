package client.logic.internalEvents;

import client.logic.InternalEventHandlerRegistry;

public interface IDisposeHandler {
	
	default void registerDisposeHandler() {
		InternalEventHandlerRegistry.addDisposeHandler(this);
	}
	
	public void dispose();

}
