package client.io.input.receivers.handlers;

import client.io.input.receivers.GameInputReceiver;

public interface IGeneralInputHandler extends IInputHandler {
	
	void handleInputs(GameInputReceiver gameInputReceiver, float delta);

}
