package client.rendering.shaders;

import org.joml.Vector3f;

import client.rendering.cameras.Camera;
import client.rendering.lights.AmbientLight;
import client.rendering.lights.DirectionalLight;
import client.rendering.materials.Material;
import client.rendering.utils.Transform;

public class TerrainShader extends BaseShader {

	public TerrainShader() {
		super("terrainVertex.glsl", "terrainFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		addUniform("transformationMatrix");
		addUniform("viewMatrix");
		addUniform("projectionMatrix");
		addUniform("sun.direction"); //Actually represents the to light vector
		addUniform("sun.colour");
		addUniform("sun.intensity");
		addUniform("ambient.colour");
		addUniform("ambient.intensity");
		
		addUniform("cameraPosition");
		
		addUniform("col");
	}
	
	public void loadTransformationMatrix(Transform transform) {
		loadMat4f(transform.getTransformationMatrix(), uniforms.get("transformationMatrix"));
	}
	
	public void loadViewMatrix(Camera cam) {
		loadMat4f(cam.getViewMatrix(), uniforms.get("viewMatrix"));
	}
	
	public void loadProjectionMatrix(Camera cam) {
		loadMat4f(cam.getProjectionMatrix(), uniforms.get("projectionMatrix"));
	}
	
	public void loadDirectionalLight(DirectionalLight<?> light) {
		loadFloat(light.getIntensity(), uniforms.get("sun.intensity"));
		loadVec3f(light.getColour(), uniforms.get("sun.colour"));
		loadVec3f(light.getDirection(), uniforms.get("sun.direction"));
	}
	
	public void loadAmbientLight(AmbientLight<?> light) {
		loadFloat(light.getIntensity(), uniforms.get("ambient.intensity"));
		loadVec3f(light.getColour(), uniforms.get("ambient.colour"));
	}
	
	public void loadMaterial(Material material) {
		//TODO load terrianMaterial to shader
	}
	
	public void loadCameraPosition(Camera cam) {
		loadVec3f(cam.getPosition(), uniforms.get("cameraPosition"));
	}
	
	public void loadColour(Vector3f colour) {
		loadVec3f(colour, uniforms.get("col"));
	}

}
