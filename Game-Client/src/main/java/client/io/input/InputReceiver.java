package client.io.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import client.io.input.receivers.handlers.IKeyInputHandler;
import client.io.input.receivers.handlers.IMouseButtonInputHandler;
import client.io.input.receivers.handlers.IMouseMovementInputHandler;
import client.utils.Globals;

public abstract class InputReceiver {
	
	protected List<IKeyInputHandler> keyInputHandlers = new ArrayList<>();
	protected List<IMouseButtonInputHandler> mouseButtonHandlers = new ArrayList<>();
	protected List<IMouseMovementInputHandler> mouseMovementInputHandlers = new ArrayList<>();
	
	protected GLFWKeyCallback keyCallback;
	protected GLFWMouseButtonCallback mouseButtonCallback;
	protected GLFWCursorPosCallback cursorPosCallback;
	protected GLFWScrollCallback scrollCallback;
	
	protected abstract void setCallBacks();
	protected abstract void freeCallbacks();
	
	public InputReceiver() {
		setCallBacks();
	}

	/**
	 * Used to bind callbacks to the window instance.
	 */
	public void activate() {
		GLFW.glfwSetCursorPosCallback(Globals.window.getId(), cursorPosCallback);
		GLFW.glfwSetMouseButtonCallback(Globals.window.getId(), mouseButtonCallback);
		GLFW.glfwSetKeyCallback(Globals.window.getId(), keyCallback);
		GLFW.glfwSetScrollCallback(Globals.window.getId(), scrollCallback);
	}
	
	public void reset() {
		setCallBacks();
	}
	 
	public void dispose() {
		freeCallbacks();
	}
	
	public void addKeyHandler(IKeyInputHandler keyHandler) {
		keyInputHandlers.add(keyHandler);
	}
	
	public void addMouseButtonHandler(IMouseButtonInputHandler mouseBtnHandler) {
		mouseButtonHandlers.add(mouseBtnHandler);
	}
	
	public void addMouseMovementHandler(IMouseMovementInputHandler mouseMovementHandler) {
		mouseMovementInputHandlers.add(mouseMovementHandler);
	}
	
}
