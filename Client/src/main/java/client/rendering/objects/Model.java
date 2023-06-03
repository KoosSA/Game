package client.rendering.objects;

import java.util.List;

import client.rendering.utils.Transform;

public class Model {
	
	private List<Mesh> meshes;
	private Transform transform;
	private String name;
	
	public Model(String name, List<Mesh> meshes) {
		this.meshes = meshes;
		transform = new Transform();
		this.name = name;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public String getName() {
		return name;
	}

}
