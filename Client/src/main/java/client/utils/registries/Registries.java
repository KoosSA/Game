package client.utils.registries;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.koossa.logger.Log;

import client.rendering.lights.AmbientLight;
import client.rendering.lights.DirectionalLight;
import client.rendering.materials.Texture2D;
import client.rendering.materials.TextureLoader;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.rendering.utils.Loader;
import client.rendering.utils.ModelLoader;

public class Registries {

	
	
	
	
	public static class Textures {
		private static Map<TextureType, Map<String, Texture2D>> texMaps = new HashMap<TextureType, Map<String, Texture2D>>();
		//private static Map<String, Texture2D> textures2D = new HashMap<>();

		static {
			for (TextureType tt : TextureType.values()) {
				texMaps.put(tt, new HashMap<String, Texture2D>());
			}
		}
		
		public static Texture2D get2DTexture(String name, TextureType textureType) {
			Map<String, Texture2D> textures2D = texMaps.get(textureType);
			Texture2D t = textures2D.getOrDefault(name, null);
			if (t == null) {
				if (name.equalsIgnoreCase("none")) {
					return null;
				}
//				t = new Texture2D(name);
//				textures2D.put(name, t);
				t = TextureLoader.createTexture(name);
				if (t != null) {
					textures2D.put(name, t);
					Log.debug(Registries.Textures.class, "Texture stored in registry: " + name);
				}
			}
			return t;
		}
		
		public static Texture2D get2DTexture(int index, TextureType textureType) {
			try {
				String name = getTexture2DNameArray(textureType)[index];
				return get2DTexture(name, textureType);
			} catch (Exception e) {
				return null;
			}
		}

		public static Collection<String> getTexture2DNameList(TextureType textureType) {
			Map<String, Texture2D> textures2D = texMaps.get(textureType);
			return textures2D.keySet();
		}
		
		public static String[] getTexture2DNameArray(TextureType textureType) {
			Map<String, Texture2D> textures2D = texMaps.get(textureType);
			return textures2D.keySet().toArray(new String[textures2D.size()]);
		}
		
		public static int getTextureIndex(String name, TextureType textureType) {
			int index = 0;
			for (Iterator<String> iterator = getTexture2DNameList(textureType).iterator(); iterator.hasNext();) {
				String n = (String) iterator.next();
				if (n.equalsIgnoreCase(name)) {
					return index;
				}
				index++;
			}
			return 0;
		}
	}

	
	
	
	
	public static class Models {
		
		private static Map<String, Model> staticModels = new HashMap<>();
		
		public static Model getStaticModel(String name) {
			Model m = staticModels.getOrDefault(name, null);
			if (m == null) {
				m = ModelLoader.loadModel(name);
				staticModels.put(name, m);
				Log.debug(Registries.Models.class, "Model added to registry: " + name);
			}
			return m;
		}
		
		public static Collection<String> getModelNameList() {
			return staticModels.keySet();
		}
		
		public static String[] getModelNameArray() {
			return staticModels.keySet().toArray(new String[staticModels.size()]);
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
		Textures.texMaps.forEach((textype, map)-> {
			map.forEach((id, tex2d) -> {
				tex2d.dispose();
			});
		});
	}

}
