package client.rendering.objects;

import org.joml.Vector3f;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.koossa.logger.Log;

import client.io.KeyBinds;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.internalEvents.IUpdatable;
import client.rendering.cameras.Camera;
import client.rendering.cameras.FirstPersonCamera;
import client.utils.Globals;

public class Player implements IUpdatable, IInputHandler {
	
	private CapsuleCollisionShape capsuleCollisionShape;
	private FirstPersonCamera camera;
	private float camXoffset = 0, camYoffset = 0, camZoffset = 0;
	private PhysicsCharacter physicsCharacter;
	private float stepHeight = 0.5f;
	private float jumpForce = 10.0f;
	private float walkSpeed = 100.0f;
	private float runSpeed = 1000.0f;
	private boolean sprinting = false;
	private Vector3f walkDir = new Vector3f(0);
	
	public Player(Camera cam) {
		if (cam instanceof FirstPersonCamera) {
			camera = (FirstPersonCamera) cam;
		} else {
			camera = new FirstPersonCamera();
		}
		registerUpdatable();
		registerInputHandler(InputStates.GAME);
		Log.debug(this, "Player object created.");
	}
	
	public Player(Camera cam, float height, float radius, float mass) {
		this(cam);
		if (Globals.physics == null) {
			Log.error(this, "Cannot create player collision object for physics is not enabled or initialised.");
			return;
		}
		capsuleCollisionShape = new CapsuleCollisionShape(radius, height);
		physicsCharacter = new PhysicsCharacter(capsuleCollisionShape, stepHeight);
		Globals.physics.addObjectToPhysicsWorld(physicsCharacter);
		
		setCameraOffests(0, height - 0.01f, 0);
		physicsCharacter.setJumpSpeed(100);
		Log.debug(this, "Physics added to player object");
	}
	
	public void setCameraOffests(float x, float y, float z) {
		camXoffset = x;
		camYoffset = y;
		camZoffset = z;
	}
	
	@Override
	public void update(float delta) {
		if (physicsCharacter == null) return;
		camera.getPosition().set(physicsCharacter.getPhysicsLocation(null));
		camera.getPosition().add(camXoffset, camYoffset, camZoffset);
		walkDir.mul(delta);
		physicsCharacter.setWalkDirection(walkDir.mul(delta).mul(sprinting ? runSpeed : walkSpeed));
		walkDir.zero();
		sprinting = false;
	}
	
	@Override
	public void handleInputs(Input input, float delta) {
		
		if (input.isKeyDown(KeyBinds.WALK_FORWARD))
			walkDir.add(camera.getForward());
		
		if (input.isKeyDown(KeyBinds.WALK_BACK))
			walkDir.sub(camera.getForward());
		
		if (input.isKeyDown(KeyBinds.WALK_RIGHT))
			walkDir.add(camera.getRight());
		
		if (input.isKeyDown(KeyBinds.WALK_LEFT))
			walkDir.sub(camera.getRight());
		
		if (input.isKeyDown(KeyBinds.SPRINT))
			sprinting = true;
		
		if (input.isKeyJustPressed(KeyBinds.JUMP) && physicsCharacter.onGround()) physicsCharacter.jump();
	}
	
	public PhysicsCharacter getPhysicsCharacter() {
		return physicsCharacter;
	}
	
	public float getStepHeight() {
		return stepHeight;
	}
	
	public float getJumpForce() {
		return jumpForce;
	}
	
	public void setJumpForce(float jumpForce) {
		Log.debug(this, "Player jump force set to: " + jumpForce);
		this.jumpForce = jumpForce;
		if (physicsCharacter == null) return;
		physicsCharacter.setJumpSpeed(jumpForce);
	}
	
	public void setStepHeight(float stepHeight) {
		Log.debug(this, "Player step height set to: " + stepHeight);
		this.stepHeight = stepHeight;
		if (physicsCharacter == null) return;
		physicsCharacter.setStepHeight(stepHeight);
	}
	
	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
	
	public void setRunSpeed(float runSpeed) {
		this.runSpeed = runSpeed;
	}
	

}
