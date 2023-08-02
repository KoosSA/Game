package client.utils;

import java.io.File;
import java.io.FilenameFilter;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import client.rendering.materials.Material;
import client.utils.registries.Registries;

public class ResourceLoader {
	
	public static void loadAllModels() {
		Log.info(ResourceLoader.class, "Starting model loading.");
		File folder = Files.getCommonFolder(CommonFolders.Models);
		String[] files = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".fbx") || name.endsWith(".obj")) {
					return true;
				}
				return false;
			}
		});
		for (int i = 0; i < files.length; i++) {
			Registries.Models.getStaticModel(files[i]);
		}
		Log.info(ResourceLoader.class, "Model loading complete.");
	}
	
	public static Material loadMaterial(String name) {
		Material mat = SaveSystem.load(Material.class, true, "Materials", name);
		return mat;
	}
	
	public static void saveMaterial(Material material, String name) {
		if (material == null) {
			Log.error(ResourceLoader.class, "Cannot save material: " + name);
		}
		material.save(true, name, "Materials");
	}

}
