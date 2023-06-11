package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import com.koossa.logger.Log;

import client.io.input.Input;
import client.io.input.InputReceiver;
import client.utils.Globals;

/**
 * Handles and distributes input events related to the game world.
 * @author Koos
 *
 */
public class GameInputReceiver extends InputReceiver {
	
	
	public GameInputReceiver(Input input) {
		super(input);
	}

	@Override
	protected void setCallBacks() {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_Q && mods == (GLFW.GLFW_MOD_CONTROL| GLFW.GLFW_MOD_ALT) && action == GLFW.GLFW_PRESS) {
					Log.error(this, "Quick quit used: Ctrl + Alt + Q");
					Globals.window.exit();
				}
				if (action==GLFW.GLFW_PRESS) input.addKeyPress(key);
				if (action == GLFW.GLFW_RELEASE) input.removeKeyPress(key);
			}
		};
		
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				input.addMouseMovement(xpos, ypos);
			}
		};
	}

	@Override
	protected void freeCallbacks() {
		keyCallback.free();
		cursorPosCallback.free();
	}

	@Override
	protected void onActivate() {
		GLFW.glfwSetCursorPos(Globals.window.getId(), (float) Globals.window.getWidth() * 0.5f, (float) Globals.window.getHeight() * 0.5f);
		GLFW.glfwSetInputMode(Globals.window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		input.resetInputs();
	}

}
