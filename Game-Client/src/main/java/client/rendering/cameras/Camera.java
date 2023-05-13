package client.rendering.cameras;

import org.joml.Matrix4f;

import client.io.input.InputStates;
import client.io.input.receivers.handlers.IGeneralInputHandler;
import client.rendering.utils.Transform;

public abstract class Camera implements IGeneralInputHandler {
	
	protected Transform transform = new Transform();
	
	public Camera() {
		registerInputHandler(InputStates.GAME);
	}
	
	public abstract Matrix4f getViewMatrix();

	public abstract Matrix4f getProjectionMatrix();
	
	public Transform getTransform() {
		return transform;
	}
	

}
