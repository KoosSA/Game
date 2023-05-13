package client.rendering.lights;

import org.joml.Vector3f;

public abstract class  BaseLight<T> {
	
	protected Vector3f position = new Vector3f();
	protected Vector3f colour = new Vector3f(1);
	protected float intensity = 1;
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColour() {
		return colour;
	}
	public BaseLight<T> setColour(Vector3f colour) {
		this.colour = colour;
		return this;
	}
	public float getIntensity() {
		return intensity;
	}
	@SuppressWarnings("unchecked")
	public T setIntensity(float intensity) {
		this.intensity = intensity;
		return (T) this;
	}
	@SuppressWarnings("unchecked")
	public T setColour(float r, float g, float b) {
		colour.set(r, g, b);
		return (T) this;
	}
	@SuppressWarnings("unchecked")
	public T setPosition(float x, float y, float z) {
		colour.set(x, y, z);
		return (T) this;
	}

}
