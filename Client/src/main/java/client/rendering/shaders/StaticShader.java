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
		addUniform("material.useBaseColourTexture");
		addUniform("material.baseColourTex");
		addUniform("material.useNormalTexture");
		addUniform("material.normalTex");
		addUniform("material.useRoughnessTexture");
		addUniform("material.roughnessTex");
		addUniform("material.useDisplacementTexture");
		addUniform("material.metallicTex");
		addUniform("material.useMetallicTexture");
		addUniform("material.displacementTex");
		addUniform("material.shineDampener");
		addUniform("material.metalness");
		addUniform("material.roughness");
		
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
		loadBoolean(material.isUseTexture(TextureType.BASE_COLOUR), uniforms.get("material.useBaseColourTexture"));
		loadBoolean(material.isUseTexture(TextureType.NORMAL), uniforms.get("material.useNormalTexture"));
		loadBoolean(material.isUseTexture(TextureType.ROUGHNESS), uniforms.get("material.useRoughnessTexture"));
		loadBoolean(material.isUseTexture(TextureType.DISPLACEMENT), uniforms.get("material.useDisplacementTexture"));
		loadBoolean(material.isUseTexture(TextureType.METALLIC), uniforms.get("material.useMetallicTexture"));
		loadFloat(material.getMetallic(), uniforms.get("material.metalness"));
		loadFloat(material.getShineDampener(), uniforms.get("material.shineDampener"));
		loadFloat(material.getRoughness(), uniforms.get("material.roughness"));
		if (material.isUseTexture(TextureType.BASE_COLOUR)) {
			loadInt(0, uniforms.get("material.baseColourTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE0);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.BASE_COLOUR).getId());
		}
		if (material.isUseTexture(TextureType.NORMAL)) {
			loadInt(1, uniforms.get("material.normalTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE1);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.NORMAL).getId());
		}
		if (material.isUseTexture(TextureType.ROUGHNESS)) {
			loadInt(2, uniforms.get("material.roughnessTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE2);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.ROUGHNESS).getId());
		}
		if (material.isUseTexture(TextureType.DISPLACEMENT)) {
			loadInt(3, uniforms.get("material.displacementTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE3);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.DISPLACEMENT).getId());
		}
		if (material.isUseTexture(TextureType.METALLIC)) {
			loadInt(4, uniforms.get("material.metallicTex"));
			GL30.glActiveTexture(GL30.GL_TEXTURE4);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, material.getTexture(TextureType.METALLIC).getId());
		}
		
	}
	
	public void loadCameraPosition(Camera cam) {
		loadVec3f(cam.getPosition(), uniforms.get("cameraPosition"));
	}

}
