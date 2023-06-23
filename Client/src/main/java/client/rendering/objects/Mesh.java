package client.rendering.objects;

import client.rendering.materials.Material;

public class Mesh {

	private Material material;
	private int vaoId;
	private int numberOfIndices;
	private String name;
	private float[] vertices;
	private int[] indices;

	public Mesh(int vaoId, Material material, int numberOfIndices, String name, float[] vertices, int[] indices) {
		this.material = material;
		this.vaoId = vaoId;
		this.numberOfIndices = numberOfIndices;
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
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
	
	public float[] getVertices() {
		return vertices;
	}
	
	public int[] getIndices() {
		return indices;
	}

}
