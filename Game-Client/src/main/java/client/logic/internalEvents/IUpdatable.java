package client.logic.internalEvents;

import client.logic.InternalRegistries;

public interface IUpdatable {
	
	default void registerUpdatable() {
		InternalRegistries.addUpdatable(this);
	}
	
	void update(float delta);

}
