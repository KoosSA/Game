package client.rendering.terrain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL46;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import client.rendering.utils.Transform;
import client.utils.Globals;
import client.utils.MathUtil;

public class ChunkGenerator {
	
	private static List<Float> verts = new ArrayList<Float>();
	private static List<Integer> inds = new ArrayList<Integer>();
	private static List<Float> heights = new ArrayList<Float>();
	private static List<Float> norms = new ArrayList<Float>();
	private static Random random = new Random();
	
	public static Chunk generateChunk(int xpos, int zpos, float length) {
		String name = xpos + "_" + zpos;
		Log.debug(ChunkGenerator.class, "Generating chunk: " + name);
		File f = new File(Files.getCommonFolderPath(CommonFolders.Saves) + "/world", name);
		if (f.exists()) {
			return loadChunkFromFile(name, length);
		}
		return genNewChunk(xpos, zpos, name, length);
	}
	
	private static Chunk loadChunkFromFile(String name, float length) {
		Log.debug(ChunkGenerator.class, "Chunk is being loaded from save file: " + name);
		ChunkData cd = SaveSystem.load(ChunkData.class, false, "world", name);
		int[] renderArr = loadModelData(true, cd.vertices, cd.normals, cd.indices);
		return new Chunk(renderArr, cd.heights, cd.transform, cd.indices.length, length);
	}
	
	private static Chunk genNewChunk(int xpos, int zpos, String name, float length) {
		Log.debug(ChunkGenerator.class, "New chunk is being created: " + name);
		verts.clear();
		inds.clear();
		heights.clear();
		norms.clear();
		Transform transform = new Transform();
		transform.setPosition(xpos * length, 0, zpos * length);
		Log.debug(ChunkGenerator.class, "Transform set to: " + transform.getPosition() + " with length of: " + length);
		generateVertices(verts, inds, heights, norms, xpos, zpos);
		ChunkData cd = new ChunkData(name, MathUtil.listToArrayFloat(verts), MathUtil.listToArrayFloat(norms), MathUtil.ListToArrayInteger(inds), 
				MathUtil.listToArrayFloat(heights), transform);
		cd.save(false, name, "world");
		int[] renderArr = loadModelData(true, cd.vertices, cd.normals, cd.indices);
		return new Chunk(renderArr, cd.heights, transform, inds.size(), length);
	}
	
	private static void generateVertices(List<Float> verts, List<Integer> inds, List<Float> heights, List<Float> norms, int camX, int camZ) {
		for (float z = 0; z <= Globals.terrain.getMaxSubTiles(); z++) {
			for (float x = 0; x <= Globals.terrain.getMaxSubTiles(); x++) {
				float height = generateHeight(x + (camX * Globals.terrain.getMaxSubTiles()), z + (camZ * Globals.terrain.getMaxSubTiles()));
				verts.add(x * Globals.terrain.getDefaultTileSize());
				verts.add(height);
				heights.add(height);
				verts.add(z * Globals.terrain.getDefaultTileSize());
				
				norms.add(0f);
				norms.add(1.0f);
				norms.add(0f);
				
			}
		}
		
		for (int z = 0; z < Globals.terrain.getMaxSubTiles(); z++) {
			for (int x = 0; x < Globals.terrain.getMaxSubTiles(); x++) {
				int topLeft = ((z * (Globals.terrain.getMaxSubTiles() + 1)) + x);
				int topRight = topLeft + 1;
				int bottomLeft = (((z + 1) * (Globals.terrain.getMaxSubTiles() + 1)) + x);
				int bottomRight = bottomLeft + 1;
//				inds.add(topLeft);
//				inds.add(bottomLeft);
//				inds.add(topRight);
//				inds.add(topRight);
//				inds.add(bottomLeft);
//				inds.add(bottomRight);
				inds.add(topLeft);
				inds.add(bottomLeft);
				inds.add(bottomRight);
				inds.add(topRight);
				inds.add(topLeft);
				inds.add(bottomRight);
			}
		}
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
		return arr;
	}
	
	private static int storeIndices(int[] indices, boolean staticDraw) {
		int id = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, id);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, staticDraw ? GL46.GL_STATIC_DRAW : GL46.GL_DYNAMIC_DRAW);
		return id;
	}

	private static int storeFloatData(int index, int size, float[] data, boolean staticDraw) {
		int vbo = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, data, staticDraw ? GL46.GL_STATIC_DRAW : GL46.GL_DYNAMIC_DRAW);
		GL46.glVertexAttribPointer(index, size, GL46.GL_FLOAT, false, 0, 0);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	private static float generateHeight(float x, float z) {
		
		float corners = (getRandomHeight(x - 1, z - 1) + getRandomHeight(x + 1, z - 1) + getRandomHeight(x - 1, z + 1)
		+ getRandomHeight(x + 1, z + 1)) / 8f;
		float sides = (getRandomHeight(x - 1, z) + getRandomHeight(x + 1, z) + getRandomHeight(x, z - 1) + getRandomHeight(x, z + 1)) / 4f;
		float center = getRandomHeight(x, z) / 2f;

		
		
		return (corners + sides + center) / 3.0f;
	}
	
	private static float getRandomHeight(float x, float z) {
		random.setSeed((long) (1254 * x + 2587 * z));
		return 5 * (random.nextFloat() - 0.5f) * 0.2f;
	}

}
