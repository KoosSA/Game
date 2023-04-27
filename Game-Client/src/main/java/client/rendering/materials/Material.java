package client.rendering.materials;

import java.util.HashMap;
import java.util.Map;

public class Material {
	
	private Map<TextureType, Texture2D> textures;
	
	public Material() {
		textures = new HashMap<>();
	}
	
	public Texture2D getTexture(TextureType type) {
		return textures.getOrDefault(type, null);
	}
	
	/**
	 * Sets the texture in the material while returning the old texture in that slot. If no texture was previously assigned returns null.
	 * @param type
	 * @param texture
	 * @return
	 */
	public Texture2D setTexture(TextureType type, Texture2D texture) {
		return textures.put(type, texture);
	}

}
