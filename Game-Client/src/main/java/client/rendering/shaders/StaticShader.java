package client.rendering.shaders;

public class StaticShader extends BaseShader {

	public StaticShader() {
		super("staticVertex.glsl", "staticFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		
	}

}
