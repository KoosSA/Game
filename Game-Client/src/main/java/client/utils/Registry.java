package client.utils;

import java.util.HashMap;
import java.util.Map;

import client.rendering.materials.Texture2D;
import client.rendering.materials.TextureType;

public class Registry {
	
	public static class Textures {
		private static Map<String, Texture2D> textures2D = new HashMap<>();
		public static Texture2D get2DTexture(String name) {
			Texture2D t = textures2D.getOrDefault(name, null);
			if (t == null) {
				t = new Texture2D(name);
				textures2D.put(name, t);
			}
			return t;
		}
	}

}
