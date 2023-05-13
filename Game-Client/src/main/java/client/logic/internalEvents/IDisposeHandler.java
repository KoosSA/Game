package client.logic.internalEvents;

import client.logic.InternalRegistries;

public interface IDisposeHandler {
	
	default void registerDisposeHandler() {
		InternalRegistries.addDisposeHandler(this);
	}
	
	public void dispose();

}
