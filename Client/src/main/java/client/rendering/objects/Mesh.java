package client.rendering.objects;

import client.rendering.materials.Material;

public class Mesh {

	private Material material;
	private int vaoId;
	private int numberOfIndices;
	private String name;

	public Mesh(int vaoId, Material material, int numberOfIndices, String name) {
		this.material = material;
		this.vaoId = vaoId;
		this.numberOfIndices = numberOfIndices;
		this.name = name;
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
	
	public String getName() {
		return name;
	}

}
