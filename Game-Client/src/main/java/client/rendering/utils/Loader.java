package client.rendering.utils;

import java.util.LinkedList;

import org.lwjgl.opengl.GL46;

import com.koossa.logger.Log;

public class Loader {
	
	private static LinkedList<Integer> vaos = new LinkedList<>();
	private static LinkedList<Integer> vbos = new LinkedList<>();
	
	public static int loadModelData(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
		int vao = GL46.glGenVertexArrays();
		GL46.glBindVertexArray(vao);
		storeFloatData(0, 3, vertices);
		storeFloatData(1, 2, texCoords);
		storeFloatData(2, 3, normals);
		storeIndices(indices);
		GL46.glEnableVertexAttribArray(0);
		GL46.glEnableVertexAttribArray(1);
		GL46.glEnableVertexAttribArray(2);
		GL46.glBindVertexArray(0);
		vaos.add(vao);
		return vao;
	}

	private static void storeIndices(int[] indices) {
		int id = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, id);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);
		vbos.add(id);
	}

	private static void storeFloatData(int index, int size, float[] data) {
		int vbo = GL46.glGenBuffers();
		vbos.add(vbo);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, data, GL46.GL_STATIC_DRAW);
		GL46.glVertexAttribPointer(index, size, GL46.GL_FLOAT, false, 0, 0);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
	}
	
	public static void dispose() {
		Log.debug(Loader.class, "Disposing vaos and vbos.");
		GL46.glBindVertexArray(0);
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		vaos.forEach(vao -> {
			GL46.glDeleteVertexArrays(vao);
		});
		vbos.forEach(vbo ->{
			GL46.glDeleteBuffers(vbo);
		});
	}

	public static int loadModelData(float[] vertices, float[] texCoords, float[] normals, int[] indices, float[] tangents, float[] bitangents) {
		int vao = GL46.glGenVertexArrays();
		GL46.glBindVertexArray(vao);
		storeFloatData(0, 3, vertices);
		storeFloatData(1, 2, texCoords);
		storeFloatData(2, 3, normals);
		storeFloatData(3, 3, tangents);
		storeFloatData(4, 3, bitangents);
		storeIndices(indices);
		GL46.glEnableVertexAttribArray(0);
		GL46.glEnableVertexAttribArray(1);
		GL46.glEnableVertexAttribArray(2);
		GL46.glEnableVertexAttribArray(3);
		GL46.glEnableVertexAttribArray(4);
		GL46.glBindVertexArray(0);
		vaos.add(vao);
		return vao;
	}

}
