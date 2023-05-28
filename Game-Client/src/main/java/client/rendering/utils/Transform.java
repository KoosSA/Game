package client.rendering.utils;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import client.utils.MathUtil;

public class Transform {

	private Vector3f position = new Vector3f();
	private Vector3f scale = new Vector3f(1);
	private Quaternionf rotation = new Quaternionf();
	private Vector3f forward = new Vector3f(0,0,-1);
	private Vector3f up = new Vector3f(0,1,0);
	
	public Matrix4f getTransformationMatrix() {
		return MathUtil.getTransformationMatrix(this);
	}

	public Vector3f getPosition() {
		return position;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Transform setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
		return this;
	}
	
	public Transform setPosition(Vector3f pos) {
		this.position.set(pos);
		return this;
	}

	public void setRotation(Quaternionf rotation) {
		this.rotation = rotation;
	}

	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
	
	public void move(float x, float y, float z) {
		this.position.add(x, y, z);
	}
	
	public void resetRotation() {
		rotation.identity();
	}
	
	public void turn(float x, float y, float z) {
		x = Math.toRadians(x);
		y = Math.toRadians(y);
		z = Math.toRadians(z);
		rotation.rotateLocalY(-y);
		rotation.rotateLocalX(-x);
		rotation.rotateLocalZ(z);
		rotation.normalize();
		forward.set(0,0,-1).rotate(rotation);
		up.set(0, 1, 0).rotate(rotation);
	}
	
	public Vector3f getPointInFrontOf(float distance, Vector3f dest) {
		return forward.mul(distance, dest);
	}

	public void setRotation(float x, float y, float z) {
		resetRotation();
		turn(x, y, z);
	}

}
