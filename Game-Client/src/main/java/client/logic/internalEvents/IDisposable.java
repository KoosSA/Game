package client.logic.internalEvents;

import client.logic.InternalRegistries;

public interface IDisposable {
	
	default void registerDisposeHandler() {
		InternalRegistries.addDisposeHandler(this);
	}
	
	public void dispose();

}
