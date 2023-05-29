package client.utils.registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import client.rendering.lights.AmbientLight;
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
				if (name.equalsIgnoreCase("none")) {
					return null;
				}
				t = new Texture2D(name);
				textures2D.put(name, t);
			}
			return t;
		}

		public static Collection<String> getTexture2DNameList() {
			return textures2D.keySet();
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
		
		public static Collection<String> getModelNameList() {
			return staticModels.keySet();
		}
	}
	
	public static class Lights {
		private static DirectionalLight<?> dirLight = (DirectionalLight<?>) new DirectionalLight<Object>(-0.5f, -0.5f, 0);
		private static AmbientLight<?> ambientLight = new AmbientLight<>(1, 1, 1, 0.05f);
		
		public static DirectionalLight<?> getDirectionalLight() {
			return dirLight;
		}
		
		public static AmbientLight<?> getAmbientLight() {
			return ambientLight;
		}
	}
	
	public static void dispose() {
		Loader.dispose();
		Textures.textures2D.forEach((id, tex2d) -> {
			tex2d.dispose();
		});
	}

}
