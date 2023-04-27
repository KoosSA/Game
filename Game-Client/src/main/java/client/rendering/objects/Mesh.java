package client.rendering.objects;

public class Mesh {
	
	private float[] vertices, normals,  texCoords;
	private int[] indices;

	

	public Mesh(float[] vertices, float[] normals, float[] texCoords, int[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		this.indices = indices;
	}



	public float[] getVertices() {
		return vertices;
	}

}
