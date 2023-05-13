package client.io.input.receivers;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import com.koossa.logger.Log;

import client.io.input.InputReceiver;
import client.logic.internalEvents.IUpdatable;
import client.utils.Globals;

/**
 * Handles and distributes input events related to the game world.
 * @author Koos
 *
 */
public class GameInputReceiver extends InputReceiver implements IUpdatable {
	
	public GameInputReceiver() {
		super();
		registerUpdatable();
	}
	
	private List<Integer> keysDown = new ArrayList<Integer>();
	private List<Integer> keysJustPressed = new ArrayList<Integer>();
	private Vector2f deltaMouse = new Vector2f();
	private Vector2f prevMouse = new Vector2f();
	private Vector2f currentMouse = new Vector2f();
	private boolean reverseMouseY = true;
	
	
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
		handleInput(this, delta);
		
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
	
	
	
	
	
	

	@Override
	protected void setCallBacks() {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_Q && mods == (GLFW.GLFW_MOD_CONTROL| GLFW.GLFW_MOD_ALT) && action == GLFW.GLFW_PRESS) {
					Log.error(this, "Quick quit used: Ctrl + Alt + Q");
					Globals.window.exit();
				}
				if (action==GLFW.GLFW_PRESS) addKeyPress(key);
				if (action == GLFW.GLFW_RELEASE) removeKeyPress(key);
			}
		};
		
		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				addMouseMovement(xpos, ypos);
			}
		};
	}

	@Override
	protected void freeCallbacks() {
		keyCallback.free();
	}

	@Override
	protected void onActivate() {
		GLFW.glfwSetCursorPos(Globals.window.getId(), Globals.window.getWidth() / 2, Globals.window.getHeight() / 2);
		GLFW.glfwSetInputMode(Globals.window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}

}
