package client.rendering.lights;

import org.joml.Vector3f;

public class DirectionalLight<T> extends BaseLight<T> {
	
	public DirectionalLight(float dirX, float dirY, float dirZ) {
		setDirection(dirX, dirY, dirZ);
	}
	
	public T setDirection(float x, float y, float z) {
		return (T) setPosition(-x, -y, -z);
	}
	
	public Vector3f getDirection() {
		return position;
	}

}
