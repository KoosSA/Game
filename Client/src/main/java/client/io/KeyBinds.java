package client.io;

import org.lwjgl.glfw.GLFW;

/**
 * Specifies the key bindings for the application. 
 * @author Koos 
 *
 */
public class KeyBinds {

	//TODO Change keybindings to a map of <String, Integer> where the string is the command and the integer is the key. 
	
	public static final int WALK_FORWARD = GLFW.GLFW_KEY_W;
	public static final int WALK_BACK = GLFW.GLFW_KEY_S;
	public static final int WALK_LEFT = GLFW.GLFW_KEY_A;
	public static final int WALK_RIGHT = GLFW.GLFW_KEY_D;
	public static final int JUMP = GLFW.GLFW_KEY_SPACE;
	public static final int CROUCH = GLFW.GLFW_KEY_LEFT_CONTROL;
	public static final int SPRINT = GLFW.GLFW_KEY_LEFT_SHIFT;
	public static final int INTERACT = GLFW.GLFW_KEY_E;

}
