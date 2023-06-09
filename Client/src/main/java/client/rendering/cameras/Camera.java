package client.rendering.cameras;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.utils.Globals;

public abstract class Camera implements IInputHandler {
	
	protected Vector3f position = new Vector3f();
	protected Vector3f scale = new Vector3f(1);
	protected float pitch = 0;
	protected float yaw = 0;
	
	protected Vector3f forward = new Vector3f(0, 0, -1);
	protected Vector3f right = new Vector3f(1, 0, 0);
	protected Vector3f up = new Vector3f(0,1,0);
	protected Vector3f direction = new Vector3f(0, 0, -1);
	protected Vector3f point = new Vector3f();
	protected float maxPitch = Math.toRadians(99);


	protected float movementSpeed = 1;
	protected float turnSpeed = 10;
	protected float sprintModifier = 10;
	
	public Camera() {
		registerInputHandler(InputStates.GAME);
		Globals.camera = this;
	}
	
	public abstract Matrix4f getViewMatrix();

	public abstract Matrix4f getProjectionMatrix();
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public Vector3f getRight() {
		return right;
	}
	
	public Vector3f getUp() {
		return up;
	}
	

}
