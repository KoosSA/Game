package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;

import client.io.input.InputReceiver;
import client.utils.Globals;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;

public class GuiInputReceiver extends InputReceiver {
	
	private Lwjgl3InputSystem inputSystem;

	public GuiInputReceiver() {
		inputSystem = Globals.gui.input_system;
	}
	
	@Override
	protected void setCallBacks() {
		GLFW.glfwSetCursorPosCallback(Globals.window.getId(), inputSystem.cursorPosCallback);
		GLFW.glfwSetMouseButtonCallback(Globals.window.getId(), inputSystem.mouseButtonCallback);
		GLFW.glfwSetKeyCallback(Globals.window.getId(), inputSystem.keyCallback);
		GLFW.glfwSetScrollCallback(Globals.window.getId(), inputSystem.scrollCallback);
	}
	
	@Override
	public void reset() {
		inputSystem = Globals.gui.input_system;
		super.reset();
	}

	@Override
	protected void freeCallbacks() {
		inputSystem.cursorPosCallback.free();
		inputSystem.keyCallback.free();
		inputSystem.mouseButtonCallback.free();
		inputSystem.scrollCallback.free();
	}

}
