package client.logic.internalEvents;

import client.logic.InternalEventRegistry;

public interface IInternalEventUpdate {
	
	default void registerUpdatable() {
		InternalEventRegistry.addUpdatable(this);
	}
	
	default void unRegisterUpdatable() {
		InternalEventRegistry.removeUpdatable(this);
	}
	
	void update(float delta);

}
