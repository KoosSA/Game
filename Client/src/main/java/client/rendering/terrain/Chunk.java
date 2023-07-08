package client.rendering.terrain;

import java.util.ArrayList;
import java.util.List;

import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.koossa.savelib.ISavable;

import client.rendering.utils.Transform;
import client.utils.Globals;
import client.utils.MathUtil;

public class Chunk implements ISavable {
	
	private float[] vertices;
	private float[] heightMap;
	private int[] indices;
	private float[] texCoords;
	private float[] normals;
	private Transform transform;
	
	public Chunk() {
		transform = new Transform();
		generateChunk();
		if (Globals.physics != null) {
			HeightfieldCollisionShape cs = new HeightfieldCollisionShape(heightMap);
			cs.setMargin(0.4f);
			PhysicsRigidBody rb = new PhysicsRigidBody(cs, 0);
			Globals.physics.addObjectToPhysicsWorld(rb);
			if (Globals.physics.isDebug()) Globals.physics.addToDebugRenderer(rb);
		}
	}

	public Chunk generateChunk() {
		List<Float> verts = new ArrayList<Float>();
		List<Integer> inds = new ArrayList<Integer>();
		List<Float> heights = new ArrayList<Float>();
		List<Float> norms = new ArrayList<Float>();
		
		generateVertices(verts, inds, heights, norms);
		
		vertices = MathUtil.listToArrayFloat(verts);
		indices = MathUtil.ListToArrayInteger(inds);
		heightMap = MathUtil.listToArrayFloat(heights);
		normals = MathUtil.listToArrayFloat(norms);
		
		return this;
	}

	private void generateVertices(List<Float> verts, List<Integer> inds, List<Float> heights, List<Float> norms) {
		if (Globals.terrain.getMaxSubTiles() <= 0) Globals.terrain.setMaxSubTiles(2);
		if ((Globals.terrain.getMaxSubTiles() % 2) != 0) Globals.terrain.setMaxSubTiles(Globals.terrain.getMaxSubTiles() + 1);
		for (float z = 0; z <= Globals.terrain.getMaxSubTiles(); z++) {
			for (float x = 0; x <= Globals.terrain.getMaxSubTiles(); x++) {
				verts.add(x * Globals.terrain.getDefaultTileSize());
				verts.add(0f);
				heights.add(0f);
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
				inds.add(topLeft);
				inds.add(bottomLeft);
				inds.add(topRight);
				inds.add(topRight);
				inds.add(bottomLeft);
				inds.add(bottomRight);
			}
		}
	}
	
	public float[] getVertices() {
		return vertices;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float[] getNormals() {
		return normals;
	}
	
	public Transform getTransform() {
		return transform;
	}

}
