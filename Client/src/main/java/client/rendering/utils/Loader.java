package client.rendering.utils;

import java.util.LinkedList;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import com.koossa.logger.Log;

public class Loader {
	
	private static LinkedList<Integer> vaos = new LinkedList<>();
	private static LinkedList<Integer> vbos = new LinkedList<>();
	
	public static int loadModelData(boolean staticDraw, float[] vertices, float[] texCoords, float[] normals, int[] indices) {
		int vao = GL46.glGenVertexArrays();
		GL46.glBindVertexArray(vao);
		storeFloatData(0, 3, vertices, staticDraw);
		storeFloatData(1, 2, texCoords, staticDraw);
		storeFloatData(2, 3, normals, staticDraw);
		storeIndices(indices, staticDraw);
		GL46.glEnableVertexAttribArray(0);
		GL46.glEnableVertexAttribArray(1);
		GL46.glEnableVertexAttribArray(2);
		GL46.glBindVertexArray(0);
		vaos.add(vao);
		return vao;
	}
	
	public static int[] loadModelData(boolean staticDraw, float[] vertices, float[] normals, int[] indices) {
		int[] arr = new int[4];
		int vao = GL46.glGenVertexArrays();
		GL46.glBindVertexArray(vao);
		arr[0] = vao;
		arr[1] = storeFloatData(0, 3, vertices, staticDraw);
		arr[2] = storeFloatData(1, 3, normals, staticDraw);
		arr[3] = storeIndices(indices, staticDraw);
		GL46.glEnableVertexAttribArray(0);
		GL46.glEnableVertexAttribArray(1);
		GL46.glBindVertexArray(0);
		vaos.add(vao);
		return arr;
	}

	public static void loadFloatBufferData(int vao, int vbo, float[] newData) {
		GL46.glBindVertexArray(vao);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, newData, GL46.GL_DYNAMIC_DRAW);
		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
			Log.error(Loader.class, "Opengl Error: " + GL30.glGetError() + " Probably something to do with loading buffer data");
		}
	}
	
	public static void loadIntBufferData(int vao, int vbo, int[] newData) {
		GL46.glBindVertexArray(vao);
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, newData, GL46.GL_DYNAMIC_DRAW);
		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
			Log.error(Loader.class, "Opengl Error: " + GL30.glGetError() + " Probably something to do with loading buffer data");
		}
	}

	private static int storeIndices(int[] indices, boolean staticDraw) {
		int id = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, id);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, staticDraw ? GL46.GL_STATIC_DRAW : GL46.GL_DYNAMIC_DRAW);
		vbos.add(id);
		return id;
	}

	private static int storeFloatData(int index, int size, float[] data, boolean staticDraw) {
		int vbo = GL46.glGenBuffers();
		vbos.add(vbo);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, data, staticDraw ? GL46.GL_STATIC_DRAW : GL46.GL_DYNAMIC_DRAW);
		GL46.glVertexAttribPointer(index, size, GL46.GL_FLOAT, false, 0, 0);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		return vbo;
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

	public static int loadModelData(boolean staticDraw, float[] vertices, float[] texCoords, float[] normals, int[] indices, float[] tangents, float[] bitangents) {
		int vao = GL46.glGenVertexArrays();
		GL46.glBindVertexArray(vao);
		storeFloatData(0, 3, vertices, staticDraw);
		storeFloatData(1, 2, texCoords, staticDraw);
		storeFloatData(2, 3, normals, staticDraw);
		storeFloatData(3, 3, tangents, staticDraw);
		storeFloatData(4, 3, bitangents, staticDraw);
		storeIndices(indices, staticDraw);
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
