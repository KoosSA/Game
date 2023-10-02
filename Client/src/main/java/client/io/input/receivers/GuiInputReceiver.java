package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.koossa.logger.Log;

import client.io.input.Input;
import client.io.input.InputReceiver;
import client.utils.Globals;


/**
 * Input receiver dealing with igui events. Most functionality should be handled by the igui system.
 */
public class GuiInputReceiver extends InputReceiver {
	
	public GuiInputReceiver(Input input) {
		super(input);
	}

//	private Lwjgl3InputSystem inputSystem = Globals.ngui.input_system;
	
	@Override
	protected void setCallBacks() {
//		inputSystem = Globals.ngui.input_system;
//		
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
				
				Globals.ngui.input_system.keyCallback.invoke(window, key, scancode, action, mods);
				Globals.igui.getGlfwImpl().keyCallback(window, key, scancode, action, mods);
			}
		};
		
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				input.addMouseMovement(xpos, ypos);
				Globals.ngui.input_system.cursorPosCallback.invoke(window, xpos, ypos);
			}
		};
		
		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				Globals.ngui.input_system.scrollCallback.invoke(window, xoffset, yoffset);
				Globals.igui.getGlfwImpl().scrollCallback(window, xoffset, yoffset);
			}
		};
		
		mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				Globals.ngui.input_system.mouseButtonCallback.invoke(window, button, action, mods);
				Globals.igui.getGlfwImpl().mouseButtonCallback(window, button, action, mods);
			}
		};
		
		charCallback = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				Globals.igui.getGlfwImpl().charCallback(window, codepoint);
			}
		};
	}
	
	@Override
	public void reset() {
		//inputSystem = Globals.igui.input_system;
		super.reset();
	}

	@Override
	protected void freeCallbacks() {
		Globals.ngui.input_system.cursorPosCallback.free();
		Globals.ngui.input_system.keyCallback.free();
		Globals.ngui.input_system.mouseButtonCallback.free();
		Globals.ngui.input_system.scrollCallback.free();
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
