package client.logic.internalEvents;

import client.logic.InternalRegistries;

public interface IUpdatable {
	
	default void registerUpdatable() {
		InternalRegistries.addUpdatable(this);
	}
	
	default void unRegisterUpdatable() {
		InternalRegistries.removeUpdatable(this);
	}
	
	void update(float delta);

}
