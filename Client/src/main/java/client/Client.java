package client;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import client.audio.Audio;
import client.gui.IGuiLayer;
import client.io.KeyBinds;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.rendering.objects.ModelInstance;
import client.rendering.utils.ModelManager;
import client.utils.Globals;
import client.utils.registries.Registries;
import common.utils.timer.ITimedEvent;
import common.utils.timer.Timer;
import imgui.ImGui;

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
	int counter = 0;
	int fsc = 1;
	int af = 75;
	
	@Override
	protected void init() {
		input.setInputReceiver(InputStates.GAME);

		Model m = Registries.Models.getStaticModel("t.fbx");
		Material mat = m.getMeshes().get(0).getMaterial();
		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
		Model m1 = Registries.Models.getStaticModel("cylinder.fbx");
		Material mat1 = m1.getMeshes().get(0).getMaterial();
		mat1.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
		mat1.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat1.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
		
		
		Audio audio = new Audio(camera.getPosition(), camera.getForward(), camera.getUp());
		int soundId = audio.getSound("test.ogg");
		audio.getPlayer("drum").addSound(soundId);
		
		
		physics.enableDebug();
		physics.setGravity(0, -10, 0);

		Globals.input.registerInputHandler(new IInputHandler() {
			
			@Override
			public void handleInputs(Input input, float delta) {
				if (input.isKeyDown(KeyBinds.INTERACT)) {
					ModelInstance mi = ModelManager.addModelInstanceToWorld(new ModelInstance(m));
					mi.getTransform().setPosition(0,100,0);
					mi.addPhysicsToInstance(1);
					mi.getRigidBody().setRestitution(1f);
					counter++;
				}
				
				if (input.isKeyDown(GLFW.GLFW_KEY_F)) {
					
								ModelInstance mi = ModelManager.addModelInstanceToWorld(new ModelInstance(m1));
								mi.getTransform().setPosition(0,10,0);
								
								mi.addPhysicsToInstance(1);
								mi.getRigidBody().setRestitution(0);
								counter++;
							
					
				}
				
				if (input.isKeyJustPressed(GLFW.GLFW_KEY_KP_ENTER)) {
					if (physics.isDebug()) {
						physics.disableDebug();
					} else {
						physics.enableDebug();
					}
				}
				
				
				
				if (input.isKeyJustPressed(GLFW.GLFW_KEY_P)) {
					ModelInstance mi = ModelManager.addModelInstanceToWorld(new ModelInstance(m1));
					mi.getTransform().setPosition((( FirstPersonCamera )camera).getPointInFrontOfCam(5));
					mi.addPhysicsToInstance(1);
					mi.getRigidBody().setCcdMotionThreshold(0.015f);
					mi.getRigidBody().applyCentralImpulse(camera.getDirection().mul(100, new Vector3f()));
					mi.getRigidBody().setRestitution(1f);
					counter++;
				}
				
				if (input.isKeyJustPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
					if (Globals.input.getCurrentInputState().equals(InputStates.GAME)) {
						Globals.input.setInputReceiver(InputStates.GUI);
						gui.show("materialSettings");
					} else {
						Globals.input.setInputReceiver(InputStates.GAME);
						gui.hide("materialSettings");
					}
				}
			}
		}, InputStates.GAME);
		
		
		
		Timer.registerNewInfiniteRepeatEventMillis(1000, 0, new ITimedEvent() {
			@Override
			public void handle() {
				af = fsc;
				fsc = 0;
			}
		});
		
		

		
		gui.addGuiLayer("fps", new IGuiLayer() {
			
			@Override
			public void create() {
				ImGui.begin("Stats");
				ImGui.text("This is the current FPS: " +  af );
				ImGui.text("PhysicsObjects in world: " + counter );
				
				ImGui.end();
			}
		});
		
		gui.show("fps");
		
		
		
		PhysicsRigidBody floor = new PhysicsRigidBody(new BoxCollisionShape(200, 0.5f, 200), 0);
		physics.addObjectToPhysicsWorld(floor);
		physics.addToDebugRenderer(floor);
		floor.setRestitution(0);
	}

	
	@Override
	protected void update(float delta) {
		fsc++;
	}

	@Override
	protected void render() {
		//renderer.baseRender();
	}

}
