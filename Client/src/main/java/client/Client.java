package client;

import org.joml.Vector3f;

import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import client.io.input.InputStates;
import client.logic.BaseGameLoop;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.utils.registries.Registries;

public class Client extends BaseGameLoop {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		//Thread.sleep(5000);
		//c.disconnect();
		new Client().start();
	}
	
	PhysicsRigidBody rb;
	Model m;
	
	@Override
	protected void init() {
		input.setInputReceiver(InputStates.GAME);

		m = Registries.Models.getStaticModel("cylinder.fbx");
		Material mat = m.getMeshes().get(0).getMaterial();
		mat.removeTexture(TextureType.BASE_COLOUR);
		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
		
		physics.enableDebug();
		
		rb = new PhysicsRigidBody(new CylinderCollisionShape(0.5f, 1.0f, 1), 0);
		rb.setPhysicsLocation(new Vector3f(0,10,0));
		physics.addObjectToPhysicsWorld(rb);
		physics.addToDebugRenderer(rb);
		
	}

	
	@Override
	protected void update(float delta) {
		m.getTransform().linkPosition(rb.getPhysicsLocation(null));
	}

	@Override
	protected void render() {
		//renderer.baseRender();
		physics.debugDraw(camera);
	}

}
