package client.io.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.koossa.logger.Log;

import client.io.input.receivers.handlers.IInputHandler;
import client.utils.Globals;

public abstract class InputReceiver {
	
	protected List<IInputHandler> inputHandlers = new ArrayList<>();
	
	protected GLFWKeyCallback keyCallback;
	protected GLFWMouseButtonCallback mouseButtonCallback;
	protected GLFWCursorPosCallback cursorPosCallback;
	protected GLFWScrollCallback scrollCallback;
	protected GLFWCharCallback charCallback;
	
	protected abstract void setCallBacks();
	protected abstract void freeCallbacks();
	protected abstract void onActivate();
	
	protected Input input;
	
	public InputReceiver(Input input) {
		this.input = input;
		setCallBacks();
	}

	/**
	 * Used to bind callbacks to the window instance.
	 */
	public void activate() {
		Log.debug(this, "Activating this input receiver.");
		GLFW.glfwSetCursorPosCallback(Globals.window.getId(), cursorPosCallback);
		GLFW.glfwSetMouseButtonCallback(Globals.window.getId(), mouseButtonCallback);
		GLFW.glfwSetKeyCallback(Globals.window.getId(), keyCallback);
		GLFW.glfwSetScrollCallback(Globals.window.getId(), scrollCallback);
		GLFW.glfwSetCharCallback(Globals.window.getId(), charCallback);
		onActivate();
	}
	
	public void reset() {
		setCallBacks();
	}
	 
	public void dispose() {
		freeCallbacks();
	}
	
	public void addInputHandler(IInputHandler generalInputHandler) {
		inputHandlers.add(generalInputHandler);
	}
	
	public void handleInput(float delta) {
		inputHandlers.forEach(handler -> {
			handler.handleInputs(input, delta);
		});
	}
	
}
