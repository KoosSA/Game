package client.physics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.util.DebugShapeFactory;
import com.jme3.system.NativeLibraryLoader;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IResizable;
import client.logic.internalEvents.IUpdatable;
import client.physics.debug.DebugColours;
import client.physics.debug.DebugModel;
import client.physics.debug.PhysicsDebugShader;
import client.rendering.cameras.Camera;
import client.utils.Globals;
import client.utils.MathUtil;

public class Physics implements IUpdatable, IDisposable, IResizable {
	
	private PhysicsSpace world;
	private Vector3f gravity;
	private boolean isInitialised = false;
	private List<PhysicsRayTestResult> rayResultList = new ArrayList<PhysicsRayTestResult>();
	private boolean canRender = false, debug = false;
	private PhysicsDebugShader shader;
	private int vao, vbo;
	private List<DebugModel> renderBuffer = new ArrayList<DebugModel>();
	private Camera cam;

	public Physics(Camera cam) {
		Log.debug(this, "Initialising physics system.");
		registerDisposeHandler();
		registerUpdatable();
		registerResizeHandler();
		Log.info(this, "Bullet native library loading result: " + (isInitialised = NativeLibraryLoader.loadLibbulletjme(true, Files.getFolder("Libs"), "Release", "Sp")));
		if (!isInitialised) {
			Log.error(this, "Failed to initialise bullet physics.");
			return;
		}
		Globals.physics = this;
		gravity = new Vector3f(0, -10, 0);
		world = new PhysicsSpace(BroadphaseType.DBVT);
		world.setGravity(gravity);
		Log.debug(this, "Physics initialisation finished.");
		shader = new PhysicsDebugShader();
		this.cam = cam;
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.stop();
	}

	@Override
	public void dispose() {
		if (!isInitialised) return;
		Log.info(this, "Disposing bullet physics.");
		canRender = false;
		shader.dispose();
		world.destroy();
		isInitialised = false;
		disposeDebugMeshes();
		shader.dispose();
	}

	@Override
	public void update(float delta) {
		if (!isInitialised) return;
		world.update(delta, 2);
	}
	
	public void setGravity(float x, float y, float z) {
		this.gravity.set(x, y, z);
		if (!isInitialised) return;
		world.setGravity(gravity);
	}
	
	public PhysicsSpace getWorld() {
		if (!isInitialised) return null;
		return world;
	}
	
	public void addObjectToPhysicsWorld(Object object) {
		if (!isInitialised) return;
		world.add(object);
	}

	public void removeObjectFromPhysicsWorld(Object object) {
		if (!isInitialised) return;
		world.remove(object);
	}
	
	public void addToDebugRenderer(Object object) {
		DebugModel dm = null;
		if (DebugModel.getCurrentList().containsKey(object)) {
			dm = DebugModel.get(object);
		} else {
			FloatBuffer fb = DebugShapeFactory.getDebugTriangles(((PhysicsCollisionObject)object).getCollisionShape() , DebugShapeFactory.highResolution);
			fb.flip();
			dm = new DebugModel(fb, (PhysicsCollisionObject) object);
		}
		renderBuffer.add(dm);
	}

	public void removeFromDebugRenderer(Object object) {
		DebugModel dm = null;
		if (DebugModel.getCurrentList().containsKey(object)) {
			dm = DebugModel.get(object);
			if (renderBuffer.contains(dm)) {
				renderBuffer.remove(dm);
			}
		}
	}
	
	private void disposeDebugMeshes() {
		if (!debug) return;
		GL46.glDeleteBuffers(vbo);
		GL30.glBindVertexArray(0);
		GL46.glDeleteVertexArrays(vao);
	}

	public void enableDebug() {
		vao = GL46.glGenVertexArrays();
		vbo = GL46.glGenBuffers();
		canRender = true;
		debug = true;
	}

	public void disableDebug() {
		canRender = false;
		disposeDebugMeshes();
		debug = false;
	}

	public void debugDraw(Camera cam) {
		if (!canRender) return;
		shader.start();
		shader.loadViewMatrix(cam.getViewMatrix());
		GL30.glDisable(GL30.GL_CULL_FACE);
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
		GL30.glLineWidth(4);

		GL30.glBindVertexArray(vao);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glEnableVertexAttribArray(0);

		GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

		for (int i = 0; i < renderBuffer.size(); i++) {
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, renderBuffer.get(i).getVerts(), GL46.GL_DYNAMIC_DRAW);

			shader.loadTransformationMatrix(MathUtil.getTransformationMatrix(renderBuffer.get(i).getRigidBody().getPhysicsLocation(null), renderBuffer.get(i).getRigidBody().getPhysicsRotation(null), renderBuffer.get(i).getRigidBody().getScale(null)));
			shader.loadColour(getDebugColour(renderBuffer.get(i).getRigidBody()));


			GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, renderBuffer.get(i).getVerts().capacity()/3);

		}

		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		GL46.glBindVertexArray(0);

		GL30.glBindVertexArray(0);
		shader.stop();
		GL30.glEnable(GL30.GL_CULL_FACE);
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
	}

	private Vector3f getDebugColour(PhysicsCollisionObject physicsCollisionObject) {
		if (physicsCollisionObject.isActive()) {
			return DebugColours.RB_AWAKE;
		} else {
			if (physicsCollisionObject.isStatic()) return DebugColours.RB_STATIC;
			return DebugColours.RB_ASLEEP;
		}

	}

	public boolean isDebug() {
		return debug;
	}
	
	public List<PhysicsRayTestResult> getRayCastResultList(Vector3f start, Vector3f end){
		if (!isInitialised) return null;
		rayResultList.clear();
		rayResultList = world.rayTest(start, end);
		return rayResultList;
	}

	public PhysicsRayTestResult getNearestRayCastResult(Vector3f start, Vector3f end) {
		if (!isInitialised) return null;
		getRayCastResultList(start, end);
		if (rayResultList.size() <= 0) {
			Log.info(this, "No ray hits were detected - returning null.");
			return null;
		}
		return rayResultList.get(0);
	}

	@Override
	public void onResize(int width, int height) {
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.stop();
	}
	
	

}
