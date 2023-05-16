package client.rendering.lights;

public class AmbientLight<T> extends BaseLight<T> {
	
	public AmbientLight(float r, float g, float b, float intensity) {
		super();
		setColour(r, g, b);
		setIntensity(intensity);
	}

}
