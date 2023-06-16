package client.rendering.materials;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector4f;

import client.utils.registries.Registries;

public class Material {
	
	//public static final Material DEFAULT = new Material();
	private Map<TextureType, Texture2D> textures;
	private Vector4f diffuseColour;
	private float metallic = 1;
	private float shineDampener = 10;
	private float roughness = 0;
	
	public Material() {
		textures = new HashMap<>();
		diffuseColour = new Vector4f(0,1,0,1);
	}
	
	public Texture2D getTexture(TextureType type) {
		return textures.getOrDefault(type, null);
	}
	
	public Material addTexture(TextureType type, String texName) {
		if (texName.toLowerCase().equals("none") || texName == null) {
			removeTexture(type);
			return this;
		}
		textures.put(type, Registries.Textures.get2DTexture(texName, type));
		return this;
	}
	
	public Vector4f getDiffuseColour() {
		return diffuseColour;
	}
	
	public Material setDiffuseColour(float r, float g, float b, float a) {
		this.diffuseColour.set(r, g, b, a);
		return this;
	}
	
	/**
	 * Sets the texture in the material while returning the old texture in that slot. If no texture was previously assigned returns null.
	 * @param type
	 * @param texture
	 * @return
	 */
	public Texture2D setTexture(TextureType type, Texture2D texture) {
		if (texture == null) {
			removeTexture(type);
			return null;
		}
		return textures.put(type, texture);
	}
	
	public Material removeTexture(TextureType type) {
		textures.remove(type);
		return this;
	}
	
	public boolean isUseTexture(TextureType type) {
		return textures.containsKey(type);
	}
	
	public float getMetallic() {
		return metallic;
	}
	
	public float getShineDampener() {
		return shineDampener;
	}
	
	public Material setMetallic(float reflectivity) {
		this.metallic = reflectivity;
		return this;
	}
	
	public Material setShineDampener(float shineDampener) {
		this.shineDampener = shineDampener;
		return this;
	}
	
	public float getRoughness() {
		return roughness;
	}
	
	public Material setRoughness(float roughness) {
		this.roughness = roughness;
		return this;
	}

}
