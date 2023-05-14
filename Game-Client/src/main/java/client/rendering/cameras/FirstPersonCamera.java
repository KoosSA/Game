package client.rendering.cameras;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.koossa.logger.Log;

import client.io.KeyBinds;
import client.io.input.receivers.GameInputReceiver;
import client.utils.MathUtil;

public class FirstPersonCamera extends Camera {

	private float fovy = 45f;
	private float near_plane = 0.01f;
	private float far_plane = 1000f;

	private Vector3f position = new Vector3f();
	private Vector3f scale = new Vector3f(1);
	private float pitch = 0;
	private float yaw = 0;
	
	private Vector3f forward = new Vector3f(0, 0, -1);
	private Vector3f right = new Vector3f(1, 0, 0);
	private Vector3f direction = new Vector3f(0, 0, -1);
	private Vector3f point = new Vector3f();
	private float maxPitch = Math.toRadians(99);


	private float movementSpeed = 1;
	private float turnSpeed = 10;
	private float sprintModifier = 5;

	public FirstPersonCamera(float fovy, float near_plane, float far_plane) {
		super();
		this.fovy = fovy;
		this.near_plane = near_plane;
		this.far_plane = far_plane;
	}

	public FirstPersonCamera() {
		super();
		//rotation.identity();
	}

	@Override
	public Matrix4f getViewMatrix() {
		return MathUtil.getViewMatrix(position, pitch, yaw, scale);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		Log.debug(this, "Updating projection matrix");
		return MathUtil.get3DProjectionMatrix(near_plane, far_plane, fovy);
	}

	@Override
	public void handleInputs(GameInputReceiver input, float delta) {
		turn(input.getMouseDeltaX() * delta * turnSpeed);
		pitch(input.getMouseDeltaY() * delta * turnSpeed);
		if (input.isKeyDown(KeyBinds.WALK_FORWARD))
			moveForward(delta, 1);
		if (input.isKeyDown(KeyBinds.WALK_BACK))
			moveForward(delta, -1);
		if (input.isKeyDown(KeyBinds.WALK_RIGHT))
			moveRight(delta, 1);
		if (input.isKeyDown(KeyBinds.WALK_LEFT))
			moveRight(delta, -1);
		if (input.isKeyDown(KeyBinds.JUMP))
			move(0, delta * movementSpeed, 0);
		if (input.isKeyDown(KeyBinds.CROUCH))
			move(0, -delta * movementSpeed, 0);
		if (input.isKeyDown(KeyBinds.SPRINT)) {
			movementSpeed = sprintModifier;
		} else {
			movementSpeed = 1;
		}
		
	}

	private void moveForward(float delta, float modifier) {
		position.add(getForward().x() * modifier * delta * movementSpeed, 0, getForward().z() * modifier * delta * movementSpeed);
	}
	
	public void move(float x, float y, float z) {
		position.add(x, y, z);
	}

	private void moveRight(float delta, float modifier) {
		position.add(getRight().x() * movementSpeed * delta * modifier, 0, getRight().z() * movementSpeed * delta * modifier);
	}

	public void turn(float angle) {
		angle = Math.toRadians(angle);
		yaw = MathUtil.loop(angle + yaw, 0, Math.toRadians(360));
		
		forward.rotateY(-angle);
		right.rotateY(-angle);
		
		forward.normalize();
		right.normalize();
	}

	public void pitch(float angle) {
		angle = Math.toRadians(-angle);
		pitch = MathUtil.clamp(pitch + angle, -maxPitch, maxPitch);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getDirection() {
		direction.set(0, 0, -1);
		direction.rotateX(-pitch);
		direction.rotateY(-yaw);
		direction.normalize();
		return direction;
	}

	private Vector3f getForward() {
		return forward;
	}

	private Vector3f getRight() {
		return right;
	}

	public Vector3f getPointInFrontOfCam(float distance) {
		position.add(getDirection().mul(distance), point);
		return point;
	}

}
