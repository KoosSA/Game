package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.koossa.logger.Log;

import client.io.input.Input;
import client.io.input.InputReceiver;
import client.utils.Globals;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;


/**
 * Input receiver dealing with gui events. Most functionality should be handled by the gui system.
 */
public class GuiInputReceiver extends InputReceiver {
	
	public GuiInputReceiver(Input input) {
		super(input);
	}

	private Lwjgl3InputSystem inputSystem = Globals.gui.input_system;
	
	@Override
	protected void setCallBacks() {
		inputSystem = Globals.gui.input_system;
		
//		cursorPosCallback = inputSystem.cursorPosCallback;
//		mouseButtonCallback = inputSystem.mouseButtonCallback;
//		keyCallback = inputSystem.keyCallback;
//		scrollCallback = inputSystem.scrollCallback;
		
		
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_Q && mods == (GLFW.GLFW_MOD_CONTROL| GLFW.GLFW_MOD_ALT) && action == GLFW.GLFW_PRESS) {
					Log.error(this, "Quick quit used: Ctrl + Alt + Q");
					Globals.window.exit();
				}
				if (action==GLFW.GLFW_PRESS) input.addKeyPress(key);
				if (action == GLFW.GLFW_RELEASE) input.removeKeyPress(key);
				
				inputSystem.keyCallback.invoke(window, key, scancode, action, mods);
			}
		};
		
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				input.addMouseMovement(xpos, ypos);
				inputSystem.cursorPosCallback.invoke(window, xpos, ypos);
			}
		};
		
		scrollCallback = new GLFWScrollCallback() {
			
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				inputSystem.scrollCallback.invoke(window, xoffset, yoffset);
			}
		};
		
		mouseButtonCallback = new GLFWMouseButtonCallback() {
			
			@Override
			public void invoke(long window, int button, int action, int mods) {
				inputSystem.mouseButtonCallback.invoke(window, button, action, mods);
			}
		};
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
		scrollCallback.free();
		keyCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();
	}

	@Override
	protected void onActivate() {
		GLFW.glfwSetCursorPos(Globals.window.getId(), Globals.window.getWidth() / 2, Globals.window.getHeight() / 2);
		GLFW.glfwSetInputMode(Globals.window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		input.resetInputs();
	}

}
