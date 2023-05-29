package client.rendering.shaders;

import org.lwjgl.opengl.GL30;

import client.rendering.cameras.Camera;
import client.rendering.lights.AmbientLight;
import client.rendering.lights.DirectionalLight;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.utils.Transform;

public class StaticShader extends BaseShader {

	public StaticShader() {
		super("staticVertex.glsl", "staticFragment.glsl");
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
		
		addUniform("material.colour");
		addUniform("material.useDiffuseTexture");
		addUniform("material.diffuseTex");
		addUniform("material.useNormalTexture");
		addUniform("material.normalTex");
		addUniform("material.useSpecularTexture");
		addUniform("material.specularTex");
		addUniform("material.useDisplacementTexture");
		addUniform("material.displacementTex");
		
		addUniform("cameraPosition");
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
		loadVec4f(material.getDiffuseColour(), uniforms.get("material.colour"));
		loadBoolean(material.isUseTexture(TextureType.DIFFUSE), uniforms.get("material.useDiffuseTexture"));
		loadBoolean(material.isUseTexture(TextureType.NORMAL), uniforms.get("material.useNormalTexture"));
		loadBoolean(material.isUseTexture(TextureType.SPECULAR), uniforms.get("material.useSpecularTexture"));
		loadBoolean(material.isUseTexture(TextureType.DISPLACEMENT), uniforms.get("material.useDisplacementTexture"));
		
		if (material.isUseTexture(TextureType.DIFFUSE)) {
			loadInt(0, uniforms.get("material.diffuseTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE0);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.DIFFUSE).getId());
		}
		if (material.isUseTexture(TextureType.NORMAL)) {
			loadInt(1, uniforms.get("material.normalTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE1);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.NORMAL).getId());
		}
		if (material.isUseTexture(TextureType.SPECULAR)) {
			loadInt(2, uniforms.get("material.specularTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE2);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.SPECULAR).getId());
		}
		if (material.isUseTexture(TextureType.DISPLACEMENT)) {
			loadInt(3, uniforms.get("material.displacementTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE3);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.DISPLACEMENT).getId());
		}
		
	}
	
	public void loadCameraPosition(Camera cam) {
		loadVec3f(cam.getPosition(), uniforms.get("cameraPosition"));
	}

}
