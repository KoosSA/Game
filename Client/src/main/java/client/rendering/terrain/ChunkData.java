package client.rendering.terrain;

import com.koossa.savelib.ISavable;

import client.rendering.utils.Transform;

public class ChunkData implements ISavable {
	
	public String name;
	public float[] vertices;
	public float[] normals;
	public int[] indices;
	public float[] heights;
	public Transform transform;
	
	public ChunkData(String name, float[] vertices, float[] normals, int[] indices, float[] heights, Transform transform) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.indices = indices;
		this.heights = heights;
		this.transform = transform;
	}
	
	

}
