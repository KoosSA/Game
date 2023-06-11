package client.io.input.receivers.handlers;

import com.koossa.logger.Log;

import client.io.input.Input;
import client.io.input.InputStates;
import client.utils.Globals;

public interface IInputHandler {
	
	default void registerInputHandler(InputStates inputState) {
		Log.debug(this, "Registering this class as a input handler for state: " + inputState.name());
		Globals.input.registerInputHandler(this, inputState);
	}
	
	default void unregisterInputHandler(InputStates inputState) {
		Log.debug(this, "Unregistering this class as a input handler for state: " + inputState.name());
		Globals.input.deregisterInputHandler(this, inputState);
	}
	
	void handleInputs(Input input, float delta);
	
	

}
