package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;

import client.io.input.InputReceiver;
import client.utils.Globals;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;


/**
 * Input receiver dealing with gui events. Most functionality should be handled by the gui system.
 */
public class GuiInputReceiver extends InputReceiver {
	
	private Lwjgl3InputSystem inputSystem = Globals.gui.input_system;
	
	@Override
	protected void setCallBacks() {
		inputSystem = Globals.gui.input_system;
		cursorPosCallback = inputSystem.cursorPosCallback;
		mouseButtonCallback = inputSystem.mouseButtonCallback;
		keyCallback = inputSystem.keyCallback;
		scrollCallback = inputSystem.scrollCallback;
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

	@Override
	protected void onActivate() {
		GLFW.glfwSetCursorPos(Globals.window.getId(), Globals.window.getWidth() / 2, Globals.window.getHeight() / 2);
		GLFW.glfwSetInputMode(Globals.window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

}
