package client.io.input;

import client.io.input.receivers.DefaultInputReceiver;
import client.io.input.receivers.GameInputReceiver;
import client.io.input.receivers.GuiInputReceiver;

public enum InputStates {
	
	/**
	 * No input is defined. Only base commands works such as CTRL + SHIFT + Q to quit. <br>
	 * Functionality is determined by the {@link DefaultInputReceiver}
	 */
	NONE,
	/**
	 * Inputs are passed on to the GUI library. <br>
	 * Functionality is handled by {@link GuiInputReceiver}
	 */
	GUI,
	/**
	 * Inputs are passed on to game events. <br>
	 * Functionality is handled by {@link GameInputReceiver}
	 */
	GAME

}
