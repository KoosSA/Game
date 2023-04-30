package client.rendering.objects;

import java.util.List;

import client.rendering.utils.Transform;

public class Model {
	
	private List<Mesh> meshes;
	private Transform transform;
	
	public Model(List<Mesh> meshes) {
		this.meshes = meshes;
		transform = new Transform();
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}
	
	public Transform getTransform() {
		return transform;
	}

}
