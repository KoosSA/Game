package client.io.input;

import java.util.HashMap;
import java.util.Map;

import com.koossa.logger.Log;

import client.io.input.receivers.DefaultInputReceiver;
import client.io.input.receivers.GameInputReceiver;
import client.io.input.receivers.GuiInputReceiver;
import client.io.input.receivers.handlers.IGeneralInputHandler;
import client.io.input.receivers.handlers.IInputHandler;
import client.io.input.receivers.handlers.IKeyInputHandler;
import client.io.input.receivers.handlers.IMouseButtonInputHandler;
import client.io.input.receivers.handlers.IMouseMovementInputHandler;
import client.logic.internalEvents.IDisposable;
import client.utils.Globals;

public class Input implements IDisposable {
	
	private Map<InputStates, InputReceiver> receivers = new HashMap<InputStates, InputReceiver>();
	private InputReceiver currentReceiver;
	private InputStates currentInputState = InputStates.NONE;
	private DefaultInputReceiver defaultReceiver = new DefaultInputReceiver();
	
	public Input() {
		Log.debug(this, "Initialising input system.");
		registerDisposeHandler();
		Globals.input = this;
		registerInputReceiver(InputStates.NONE, defaultReceiver);
		registerInputReceiver(InputStates.GUI, new GuiInputReceiver());
		registerInputReceiver(InputStates.GAME, new GameInputReceiver());
		
		setInputReceiver(InputStates.NONE);
		Log.debug(this, "Input system initialised");
	}
	
	private void registerInputReceiver(InputStates state, InputReceiver receiver) {
		Log.debug(this, "Adding input receiver (" + receiver + ") to state: " + state);
		receivers.putIfAbsent(state, receiver);
	}

	public void setInputReceiver(InputStates state) {
		Log.debug(this, "Changing input receivers to: " + state);
		currentInputState = state;
		currentReceiver = receivers.getOrDefault(state, defaultReceiver);
		if (currentReceiver != null) currentReceiver.activate();
	}
	
	public InputReceiver getInputReceiver(InputStates state) {
		return receivers.getOrDefault(state, defaultReceiver);
	}

	@Override
	public void dispose() {
		Log.debug(this, "Starting diaposal of input system");
		receivers.values().forEach(rec -> {
			rec.dispose();
		});
		Log.debug(this, "Input system disposed.");
	}
	
	public void registerInputHandler(IInputHandler handler, InputStates inputState) {
		if (handler instanceof IKeyInputHandler) receivers.get(inputState).addKeyHandler((IKeyInputHandler) handler);
		if (handler instanceof IMouseButtonInputHandler) receivers.get(inputState).addMouseButtonHandler((IMouseButtonInputHandler) handler);
		if (handler instanceof IMouseMovementInputHandler) receivers.get(inputState).addMouseMovementHandler((IMouseMovementInputHandler) handler);
		if (handler instanceof IGeneralInputHandler) receivers.get(inputState).addGeneralInputHandler((IGeneralInputHandler) handler);
	}
	
	public InputReceiver getCurrentReceiver() {
		return currentReceiver;
	}
	
	public InputStates getCurrentInputState() {
		return currentInputState;
	}

	public void deregisterInputHandler(IInputHandler handler, InputStates inputState) {
		InputReceiver receiver = receivers.get(inputState);
		if (handler instanceof IKeyInputHandler) {
			if (receiver.keyInputHandlers.contains(handler)) {
				receiver.keyInputHandlers.remove(handler);
			}
		}
		if (handler instanceof IMouseButtonInputHandler) {
			if (receiver.mouseButtonHandlers.contains(handler)) {
				receiver.mouseButtonHandlers.remove(handler);
			}
		}
		if (handler instanceof IMouseMovementInputHandler) {
			if (receiver.mouseMovementInputHandlers.contains(handler)) {
				receiver.mouseMovementInputHandlers.remove(handler);
			}
		}
		if (handler instanceof IGeneralInputHandler) {
			if (receiver.generalInputHandlers.contains(handler)) {
				receiver.generalInputHandlers.remove(handler);
			}
		}
		
	}

	
	
}
