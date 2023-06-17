package client.rendering.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.rendering.objects.Model;
import client.rendering.objects.ModelInstance;

public class ModelManager {
	
	private static Map<Model, List<ModelInstance>> instances = new HashMap<Model, List<ModelInstance>>();
	
	
	public static Map<Model, List<ModelInstance>> getInstances() {
		return instances;
	}
	
	public static void removeModelInstanceFromWorld(ModelInstance instance) {
		List<ModelInstance> list = instances.getOrDefault(instance.getModel(), null);
		if (list == null) {
			return;
		}
		list.remove(instance);
		if (list.size() <= 0) {
			instances.remove(instance.getModel());
		}
	}
	
	public static ModelInstance addModelInstanceToWorld(ModelInstance instance) {
		List<ModelInstance> list = instances.getOrDefault(instance.getModel(), null);
		if (list == null) {
			list = new ArrayList<ModelInstance>();
			instances.put(instance.getModel(), list);
		}
		list.add(instance);
		return instance;
	}
	
	

}
