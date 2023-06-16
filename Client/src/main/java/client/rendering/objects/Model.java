package client.rendering.objects;

import java.util.List;

import client.rendering.utils.Transform;

public class Model {
	
	private List<Mesh> meshes;
	private String[] meshNames;
	private Transform transform;
	private String name;
	
	public Model(String name, List<Mesh> meshes) {
		this.meshes = meshes;
		transform = new Transform();
		this.name = name;
		generateMeshNameList();
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
	
	private String[] generateMeshNameList() {
		meshNames = new String[meshes.size()];
		for (int i = 0; i < meshes.size(); i++) {
			meshNames[i] = meshes.get(i).getName();
		}
		return meshNames;
	}
	
	public String[] getMeshNames() {
		return meshNames;
	}

}
