package client.physics.debug;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import client.rendering.shaders.BaseShader;

public class PhysicsDebugShader extends BaseShader {

	public PhysicsDebugShader() {
		super("pdVertex.glsl", "pdFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		addUniform("projectionMatrix");
		addUniform("transformationMatrix");
		addUniform("viewMatrix");
		addUniform("colour");
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMat4f(projection, uniforms.get("projectionMatrix"));
	}

	public void loadTransformationMatrix(Matrix4f transformation) {
		loadMat4f(transformation, uniforms.get("transformationMatrix"));
	}

	public void loadViewMatrix(Matrix4f view) {
		loadMat4f(view, uniforms.get("viewMatrix"));
	}

	public void loadColour(Vector3f colour) {
		loadVec3f(colour, uniforms.get("colour"));
	}

}
