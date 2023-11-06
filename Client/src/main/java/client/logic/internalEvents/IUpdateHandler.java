package client.logic.internalEvents;

import client.logic.InternalEventHandlerRegistry;

public interface IUpdateHandler {
	
	default void registerUpdatable() {
		InternalEventHandlerRegistry.addUpdatable(this);
	}
	
	default void unRegisterUpdatable() {
		InternalEventHandlerRegistry.removeUpdatable(this);
	}
	
	void update(float delta);

}
