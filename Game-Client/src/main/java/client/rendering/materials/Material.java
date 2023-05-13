package client.rendering.materials;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector4f;

public class Material {
	
	private Map<TextureType, Texture2D> textures;
	private Vector4f diffuseColour;
	private boolean useTexture;
	
	public Material() {
		textures = new HashMap<>();
		diffuseColour = new Vector4f(0,0,0,1);
		useTexture = false;
	}
	
	public Texture2D getTexture(TextureType type) {
		return textures.getOrDefault(type, null);
	}
	
	public Vector4f getDiffuseColour() {
		return diffuseColour;
	}
	
	public void setDiffuseColour(float r, float g, float b, float a) {
		this.diffuseColour.set(r, g, b, a);
	}
	
	/**
	 * Sets the texture in the material while returning the old texture in that slot. If no texture was previously assigned returns null.
	 * @param type
	 * @param texture
	 * @return
	 */
	public Texture2D setTexture(TextureType type, Texture2D texture) {
		if (type == TextureType.DIFFUSE) useTexture = true;
		return textures.put(type, texture);
	}
	
	public boolean isUseTexture() {
		return useTexture;
	}

}
