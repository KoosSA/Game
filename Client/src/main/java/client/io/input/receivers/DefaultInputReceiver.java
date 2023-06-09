package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import com.koossa.logger.Log;

import client.io.input.Input;
import client.io.input.InputReceiver;
import client.utils.Globals;

public class DefaultInputReceiver extends InputReceiver {

	public DefaultInputReceiver(Input input) {
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
			}
		};
	}

	@Override
	protected void freeCallbacks() {
		keyCallback.free();
	}

	@Override
	protected void onActivate() {
		
	}

}
