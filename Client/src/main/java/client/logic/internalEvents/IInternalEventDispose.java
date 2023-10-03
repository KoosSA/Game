package client.logic.internalEvents;

import client.logic.InternalEventRegistry;

public interface IInternalEventDispose {
	
	default void registerDisposeHandler() {
		InternalEventRegistry.addDisposeHandler(this);
	}
	
	public void dispose();

}
