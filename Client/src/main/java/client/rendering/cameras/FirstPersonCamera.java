package client.rendering.cameras;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.koossa.logger.Log;

import client.io.KeyBinds;
import client.io.input.Input;
import client.utils.MathUtil;

public class FirstPersonCamera extends Camera {

	private float fovy = 45f;
	private float near_plane = 0.01f;
	private float far_plane = 1000f;

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
	public void handleInputs(Input input, float delta) {
		turn(input.getMouseDeltaX() * delta * turnSpeed);
		pitch(input.getMouseDeltaY() * delta * turnSpeed);
//		if (input.isKeyDown(KeyBinds.WALK_FORWARD))
//			moveForward(delta, 1);
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
	
	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getDirection() {
		direction.set(0, 0, -1);
		direction.rotateX(-pitch);
		direction.rotateY(-yaw);
		direction.normalize();
		return direction;
	}

	@Override
	public Vector3f getForward() {
		return forward;
	}

	@Override
	public Vector3f getRight() {
		return right;
	}
	
	@Override
	public Vector3f getUp() {
		forward.cross(right, up);
		return up.negate();
	}

	public Vector3f getPointInFrontOfCam(float distance) {
		position.add(getDirection().mul(distance), point);
		return point;
	}

}
