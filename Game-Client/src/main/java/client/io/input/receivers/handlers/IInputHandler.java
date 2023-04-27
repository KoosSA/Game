package client.io.input.receivers.handlers;

import com.koossa.logger.Log;

import client.io.input.InputStates;
import client.utils.Globals;

public interface IInputHandler {
	
	default void registerInputHandler(InputStates inputState) {
		Log.debug(this, "Registering this class as a input handler.");
		Globals.input.registerInputHandler(this, inputState);
	}

}
