package client.rendering.objects;

import java.util.List;

public class Model {
	
	private List<Mesh> meshes;
	
	public Model(List<Mesh> meshes) {
		this.meshes = meshes;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

}
