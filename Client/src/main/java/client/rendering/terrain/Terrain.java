package client.rendering.terrain;

import java.util.ArrayList;
import java.util.List;

import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import client.rendering.utils.Loader;
import client.utils.Globals;
import client.utils.MathUtil;

public class Terrain {
	
	private int maxSubTiles = 15;
	private float defaultTileSize = 1;
	private List<Chunk> torender = new ArrayList<Chunk>();
	private int vaoId, vboV, vboI, vboN;
	private int renderDistance = 2;
	
	public Terrain() {
		Globals.terrain = this;
		int[] pointers = Loader.loadModelData(false, new float[3], new float[3], new int[3]);
		vaoId = pointers[0];
		vboV = pointers[1];
		vboN = pointers[2];
		vboI = pointers[3];
		
		
		Chunk c = getChunk();
		c.getTransform().setPosition(-maxSubTiles / 2, 0, -maxSubTiles / 2);
	}
	
	public Chunk getChunk() {
		Chunk c = SaveSystem.load(Chunk.class, false, "world", "chunk_test.json");
		torender.add(c);
		updateTerrainRenderData();
		return c;
	}
	
	public float getDefaultTileSize() {
		return defaultTileSize;
	}
	
	public int getMaxSubTiles() {
		return maxSubTiles;
	}
	
	public void setMaxSubTiles(int maxSubTiles) {
		this.maxSubTiles = maxSubTiles;
	}
	
	public List<Chunk> getChunksToRender() {
		return torender;
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	private void updateTerrainRenderData() {
		Log.debug(this, "Updating terrian render buffers.");
		List<Float> v = new ArrayList<Float>();
		List<Float> n = new ArrayList<Float>();
		List<Integer> i = new ArrayList<Integer>();
		
		getChunksToRender().forEach(c -> {
			for (float val : c.getVertices()) {
				v.add(val);
			}
			for (float val : c.getNormals()) {
				n.add(val);
			}
			for (int val : c.getIndices()) {
				i.add(val);
			}
		});
		
		//System.out.println(v);
		Loader.loadFloatBufferData(vaoId, vboV, MathUtil.listToArrayFloat(v));
		Loader.loadFloatBufferData(vaoId, vboN, MathUtil.listToArrayFloat(n));
		Loader.loadIntBufferData(vaoId, vboI, MathUtil.ListToArrayInteger(i));
	}

	
	

}
