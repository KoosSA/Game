package client.terrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Math;

import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IUpdatable;
import client.utils.Globals;

public class Terrain implements IUpdatable, IDisposable {
	
	private int maxSubTiles = 15;
	private int defaultTileSize = 1;
	private Map<String, Chunk> torender = new HashMap<String, Chunk>();
	private int renderChunksX = 2;
	private int renderChunksZ = 10;
	private int prevcx = -9999999, prevcz = -99999999;
	
	public Terrain() {
		Globals.terrain = this;
	}
	
	public void createTerrain(int renderX, int renderZ, int numberOfSubTilesPerChunk) {
		registerUpdatable();
		registerDisposeHandler();
		this.maxSubTiles = numberOfSubTilesPerChunk;
		this.renderChunksX = renderX;
		this.renderChunksZ = renderZ;
		if (maxSubTiles <= 0) Globals.terrain.setMaxSubTiles(2);
		if ((maxSubTiles % 2) != 0) Globals.terrain.setMaxSubTiles(Globals.terrain.getMaxSubTiles() + 1);
	}
	
	@Override
	public void update(float delta) {
		updateChunksInRenderDistance();
	}
	
	private void updateChunksInRenderDistance() {
		float length = defaultTileSize * maxSubTiles;
		int camx = (int) Math.floor(Globals.camera.getPosition().x() / length);
		int camz = (int) Math.floor(Globals.camera.getPosition().z() / length);
		if (prevcx != camx || prevcz != camz) {
			List<String> toremove = new ArrayList<String>(torender.keySet());
			for (int x = -renderChunksX; x <= renderChunksX; x++) {
				for (int z = -renderChunksZ; z <= renderChunksZ; z++) {
					int px = camx + x;
					int pz = camz + z;
					String name = px + "_" + pz;
					if (!toremove.remove(name)) {
						torender.put(name, ChunkGenerator.generateChunk(px, pz, length));
					}
				}
			}
			toremove.forEach(rn -> {
				Chunk c = torender.remove(rn);
				if (c != null) {
					c.unloadChunk();
				}
			});
			prevcx = camx;
			prevcz = camz;
		}
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
	
	public Collection<Chunk> getChunksToRender() {
		return torender.values();
	}

	@Override
	public void dispose() {
		torender.values().forEach(c -> {
			c.unloadChunk();
		});
	}
	

}
