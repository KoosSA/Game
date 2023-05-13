package client.utils.registries;

import java.util.HashMap;
import java.util.Map;

import client.rendering.lights.DirectionalLight;
import client.rendering.materials.Texture2D;
import client.rendering.objects.Model;
import client.rendering.utils.Loader;
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
		private static Map<String, Model> staticModels = new HashMap<>();
		public static Model getStaticModel(String name) {
			Model m = staticModels.getOrDefault(name, null);
			if (m == null) {
				m = ModelLoader.loadModel(name);
				staticModels.put(name, m);
			}
			return m;
		}
	}
	
	public static class Lights {
		private static DirectionalLight<?> dirLight = (DirectionalLight<?>) new DirectionalLight<Object>(0, -1, 0).setIntensity(10);
		
		public static DirectionalLight<?> getDirectionalLight() {
			return dirLight;
		}
	}
	
	public static void dispose() {
		Loader.dispose();
		Textures.textures2D.forEach((id, tex2d) -> {
			tex2d.dispose();
		});
	}

}
