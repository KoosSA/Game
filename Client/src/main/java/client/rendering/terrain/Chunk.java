package client.rendering.terrain;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import client.rendering.utils.Transform;
import client.utils.Globals;

public class Chunk {
	
	private Transform transform;
	private PhysicsRigidBody rb;
	private int numIndices, vaoId, vboV, vboI, vboN;

	public Chunk(int[] renderArr, float[] heights, Transform transform, int numIndices, int chunkLength) {
		this.transform = transform;
		this.numIndices	=	numIndices;
		this.vaoId = renderArr[0];
		vboV = renderArr[1];
		vboN = renderArr[2];
		vboI = renderArr[3];
		if (Globals.physics != null) {
			HeightfieldCollisionShape cs = new HeightfieldCollisionShape(heights);
			cs.setMargin(0.4f);
			rb = new PhysicsRigidBody(cs, 0);
			rb.setPhysicsLocation(new Vector3f(transform.getPosition().x() + (0.5f * (float) chunkLength), 0, transform.getPosition().z() + (0.5f * (float) chunkLength)));
			Globals.physics.addObjectToPhysicsWorld(rb);
			if (Globals.physics.isDebug()) Globals.physics.addToDebugRenderer(rb);
		}
	}
	
	public void unloadChunk() {
		GL30.glDeleteVertexArrays(vaoId);
		GL30.glDeleteBuffers(vboI);
		GL30.glDeleteBuffers(vboN);
		GL30.glDeleteBuffers(vboV);
		if (Globals.physics != null) {
			Globals.physics.removeObjectFromPhysicsWorld(rb);
			if (Globals.physics.isDebug()) Globals.physics.removeFromDebugRenderer(rb);
		}
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getNumIndices() {
		return numIndices;
	}
	
	public Transform getTransform() {
		return transform;
	}

}
