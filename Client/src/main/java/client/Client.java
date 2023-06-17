package client;

import org.joml.Math;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.koossa.logger.Log;

import client.io.KeyBinds;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.BaseGameLoop;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.rendering.objects.ModelInstance;
import client.rendering.utils.ModelManager;
import client.utils.Globals;
import client.utils.registries.Registries;
import common.utils.timer.ITimedEvent;
import common.utils.timer.Timer;

public class Client extends BaseGameLoop {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		//Thread.sleep(5000);
		//c.disconnect();
		new Client().start();
	}
	
	//PhysicsRigidBody rb;
	//Model m;
	float avefps = 600;
	int counter = 0;
	
	@Override
	protected void init() {
		input.setInputReceiver(InputStates.GAME);

		Model m = Registries.Models.getStaticModel("cylinder.fbx");
		
		Material mat = m.getMeshes().get(0).getMaterial();
		
		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
		
		Timer.registerNewInfiniteRepeatEventMillis(5000, 1, new ITimedEvent() {
			@Override
			public void handle() {
				Log.info(this, avefps + " @ " + counter);
			}
		});
		
		
		
		Globals.input.registerInputHandler(new IInputHandler() {
			
			@Override
			public void handleInputs(Input input, float delta) {
				if (input.isKeyDown(KeyBinds.INTERACT)) {
					ModelInstance mi = ModelManager.addModelInstanceToWorld(new ModelInstance(m));
					mi.getTransform().setPosition(0,100,0);
					mi.addPhysicsToInstance(new CylinderCollisionShape(0.5f, 1.0f, 1), 1, true);
					counter++;
				}
			}
		}, InputStates.GAME);
		
		
		physics.enableDebug();
		physics.setGravity(0, -10, 0);
		
//		rb = new PhysicsRigidBody(new CylinderCollisionShape(0.5f, 1.0f, 1), 1);
//		physics.addObjectToPhysicsWorld(rb);
//		physics.addToDebugRenderer(rb);
//		
//		m.getTransform().setPosition(rb.getTransform(null).getTranslation());
		
		
		PhysicsRigidBody floor = new PhysicsRigidBody(new BoxCollisionShape(100, 1, 100), 0);
		physics.addObjectToPhysicsWorld(floor);
		physics.addToDebugRenderer(floor);
	}

	
	@Override
	protected void update(float delta) {
		avefps = (avefps + 1/delta) * 0.5f;
	}

	@Override
	protected void render() {
		//renderer.baseRender();
		physics.debugDraw(camera);
	}

}
