package client.rendering.objects;

import client.rendering.materials.Material;

public class Mesh {
	
	private float[] vertices, normals,  texCoords;
	private int[] indices;
	private Material material;

	

	public Mesh(float[] vertices, float[] normals, float[] texCoords, int[] indices, Material material) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.indices = indices;
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}
	
	public float[] getVertices() {
		return vertices;
	}

}
