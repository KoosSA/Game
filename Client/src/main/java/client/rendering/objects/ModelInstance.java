package client.rendering.objects;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.HullCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.koossa.logger.Log;

import client.logic.InternalRegistries;
import client.logic.internalEvents.IUpdatable;
import client.rendering.utils.Transform;
import client.utils.Globals;
import client.utils.registries.Registries;

public class ModelInstance {

	private Model model;
	private Transform transform;
	private PhysicsRigidBody rigidBody;
	private IUpdatable physicsLink;

	public ModelInstance(Model model) {
		this.model = model;
		this.transform = new Transform();
	}
	
	public ModelInstance(String modelName) {
		this.model = Registries.Models.getStaticModel(modelName);
		this.transform = new Transform();
	}
	
	public ModelInstance addPhysicsToInstance(float mass) {
		CompoundCollisionShape cs = new CompoundCollisionShape();
		model.getConvexHulls().forEach(hull -> {
			cs.addChildShape(new HullCollisionShape(hull));
		});
		return addPhysicsToInstance(cs, mass);
	}

	public ModelInstance addPhysicsToInstance(CollisionShape collisionShape, float mass) {
		if (Globals.physics == null) {
			Log.error(this, "Physics not enabled in this world.");
			return this;
		}
		rigidBody = new PhysicsRigidBody(collisionShape, mass);
		Globals.physics.addObjectToPhysicsWorld(rigidBody);
		rigidBody.setPhysicsLocation(transform.getPosition());
		rigidBody.getTransform(null).getRotation().set(transform.getRotation());
		rigidBody.getTransform(null).setScale(transform.getScale().x());
		if (mass > 0) {
			physicsLink = new IUpdatable() {
				@Override
				public void update(float delta) {
					if (rigidBody.isActive()) {
						transform.setPosition(rigidBody.getTransform(null).getTranslation());
						transform.setRotation(rigidBody.getTransform(null).getRotation());
						transform.setScale(rigidBody.getTransform(null).getScale());
					}
				}
			};
			InternalRegistries.addUpdatable(physicsLink);
		}
		if (Globals.physics.isDebug()) Globals.physics.addToDebugRenderer(rigidBody);
		return this;
	}

	public ModelInstance removeFromPhysicsWorld() {
		if (Globals.physics == null || rigidBody == null) {
			Log.error(this, "Physics not enabled in this world for this object. (Thrown while trying to remove modelinstance)");
			return this;
		}
		InternalRegistries.removeUpdatable(physicsLink);
		Globals.physics.removeObjectFromPhysicsWorld(rigidBody);
		Globals.physics.removeFromDebugRenderer(rigidBody);
		rigidBody = null;
		return this;
	}

	public Transform getTransform() {
		return transform;
	}

	public Model getModel() {
		return model;
	}

	public PhysicsRigidBody getRigidBody() {
		return rigidBody;
	}

}
