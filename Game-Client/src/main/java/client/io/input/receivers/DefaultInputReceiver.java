package client.io.input.receivers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import com.koossa.logger.Log;

import client.io.input.InputReceiver;
import client.utils.Globals;

public class DefaultInputReceiver extends InputReceiver {
	
	private static final GLFWKeyCallback defaultKeyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW.GLFW_KEY_Q && mods == (GLFW.GLFW_MOD_CONTROL| GLFW.GLFW_MOD_ALT) && action == GLFW.GLFW_PRESS) {
				Log.error(this, "Quick quit used: Ctrl + Alt + Q");
				Globals.window.exit();
			}
		}
	};

	@Override
	protected void setCallBacks() {
		Log.debug(this, "Activating default input state callbacks.");
		GLFW.glfwSetKeyCallback(Globals.window.getId(), defaultKeyCallback);
	}

	@Override
	protected void freeCallbacks() {
		defaultKeyCallback.free();
	}

}
