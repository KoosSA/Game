package client.rendering.objects;

import client.rendering.materials.Material;

public class Mesh {

	private Material material;
	private int vaoId;
	private int numberOfIndices;

	public Mesh(int vaoId, Material material, int numberOfIndices) {
		this.material = material;
		this.vaoId = vaoId;
		this.numberOfIndices = numberOfIndices;
	}

	public Material getMaterial() {
		return material;
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getNumberOfIndices() {
		return numberOfIndices;
	}

}
