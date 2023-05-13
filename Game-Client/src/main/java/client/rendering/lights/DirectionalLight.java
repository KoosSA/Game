package client.rendering.lights;

import org.joml.Vector3f;

public class DirectionalLight<T> extends BaseLight<T> {
	
	public DirectionalLight(float dirX, float dirY, float dirZ) {
		position.set(dirX, dirY, dirZ);
	}
	
	public T setDirection(float r, float g, float b) {
		return (T) setPosition(r, g, b);
	}
	
	public Vector3f getDirection() {
		return position;
	}

}
