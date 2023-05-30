package client.utils;

import java.io.File;
import java.io.FilenameFilter;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;

import client.utils.registries.Registries;

public class ResourceLoader {
	
	public static void loadAllModels() {
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
	}
	
	public static void loadAllTextures() {
		File folder = Files.getCommonFolder(CommonFolders.Textures);
		String[] files = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jpg") || name.endsWith(".png")) {
					return true;
				}
				return false;
			}
		});
		for (int i = 0; i < files.length; i++) {
			Registries.Textures.get2DTexture(files[i]);
		}
	}

}
