package client.rendering.cameras;

import org.joml.Matrix4f;

import client.rendering.utils.Transform;

public abstract class Camera {
	
	private Transform transform;
	
	public Camera() {
		transform = new Transform();
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f getViewMatrix();

}
