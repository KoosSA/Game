package client.utils;

import java.util.HashMap;
import java.util.Map;

import client.rendering.materials.Texture2D;
import client.rendering.objects.Model;
import client.rendering.utils.ModelLoader;

public class Registries {

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

	public static class Models {
		private static Map<String, Model> models = new HashMap<>();
		private static ModelLoader l = new ModelLoader();
		public static Model getModel(String name) {
			Model m = models.getOrDefault(name, null);
			if (m == null) {
				m = l.loadModel(name);
				models.put(name, m);
			}
			return m;
		}
	}

}
