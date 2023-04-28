package client.rendering.utils;

import java.util.ArrayList;
import java.util.List;

import client.rendering.objects.Model;

public class RenderManager {
	
	private static List<Model> staticModels = new ArrayList<>();
	
	public static List<Model> getStaticModels() {
		return staticModels;
	}
}
