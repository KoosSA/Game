package client.io.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;

import com.koossa.logger.Log;

import client.io.input.receivers.DefaultInputReceiver;
import client.io.input.receivers.GameInputReceiver;
import client.io.input.receivers.GuiInputReceiver;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IUpdatable;
import client.utils.Globals;

public class Input implements IDisposable, IUpdatable {
	
	private Map<InputStates, InputReceiver> receivers = new HashMap<InputStates, InputReceiver>();
	private InputReceiver currentReceiver;
	private InputStates currentInputState = InputStates.NONE;
	private DefaultInputReceiver defaultReceiver = new DefaultInputReceiver(this);
	
	private List<Integer> keysDown = new ArrayList<Integer>();
	private List<Integer> keysJustPressed = new ArrayList<Integer>();
	private Vector2f deltaMouse = new Vector2f();
	private Vector2f prevMouse = new Vector2f();
	private Vector2f currentMouse = new Vector2f();
	private boolean reverseMouseY = true;
	
	public Input() {
		Log.debug(this, "Initialising input system.");
		registerDisposeHandler();
		registerUpdatable();
		Globals.input = this;
		registerInputReceiver(InputStates.NONE, defaultReceiver);
		registerInputReceiver(InputStates.GUI, new GuiInputReceiver(this));
		registerInputReceiver(InputStates.GAME, new GameInputReceiver(this));
		
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
	
	public void resetInputs() {
		keysDown.clear();
		keysJustPressed.clear();
		currentMouse.set((float) Globals.window.getWidth() * 0.5f, (float) Globals.window.getHeight() * 0.5f);
		deltaMouse.set(0, 0);
		prevMouse.set((float) Globals.window.getWidth() * 0.5f, (float) Globals.window.getHeight() * 0.5f);
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
		receivers.get(inputState).addInputHandler(handler);
	}
	
	public InputReceiver getCurrentReceiver() {
		return currentReceiver;
	}
	
	public InputStates getCurrentInputState() {
		return currentInputState;
	}

	public void deregisterInputHandler(IInputHandler handler, InputStates inputState) {
		InputReceiver receiver = receivers.get(inputState);
		if (receiver.inputHandlers.contains(handler)) {
			receiver.inputHandlers.remove(handler);
		}
	}
	
	public boolean isKeyDown(int key) {
		return keysDown.contains(key);
	}

	public boolean isKeyJustPressed(int key) {
		return keysJustPressed.contains(key);
	}

	public float getMouseDeltaX() {
		return deltaMouse.x();
	}

	public float getMouseDeltaY() {
		return (reverseMouseY ? deltaMouse.y() * -1 : deltaMouse.y());
	}
	
	public void update(float delta) {
		currentReceiver.handleInput(delta);
		
		keysDown.addAll(keysJustPressed);
		keysJustPressed.clear();
		deltaMouse.set(currentMouse.x() - prevMouse.x(), currentMouse.y() - prevMouse.y());
		prevMouse.set(currentMouse);
	}
	
	public void addKeyPress(int key) {
		boolean down = keysDown.contains(key);
		boolean press = keysJustPressed.contains(key);
		if (!down && !press) {
			keysJustPressed.add(key);
		}
	}

	public void removeKeyPress(int key) {
		boolean down = keysDown.contains(key);
		boolean press = keysJustPressed.contains(key);
		if (down) {
			keysDown.remove((Object) key);
		}
		if (press) {
			keysJustPressed.remove((Object) key);
		}
	}

	public void addMouseMovement(double x, double y) {
		currentMouse.set(x, y);
	}

	public void setPrevMouse(float x, float y) {
		prevMouse.set(x, y);
	}

	
	
}
