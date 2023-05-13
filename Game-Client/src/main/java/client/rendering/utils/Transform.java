package client.rendering.utils;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import client.utils.MathUtil;

public class Transform {

	private Vector3f position = new Vector3f();
	private Vector3f offset = new Vector3f();
	private Vector3f scale = new Vector3f(1);
	private Quaternionf rotation = new Quaternionf();
	private static Vector3f absPos = new Vector3f();
	private Vector3f forward = new Vector3f(0,0,-1);
	private Vector3f up = new Vector3f(0,1,0);

	public Matrix4f getTransformationMatrix() {
		return MathUtil.getTransformationMatrix(this);
	}

	public Vector3f getOffset() {
		return offset;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getAbsolutePosition() {
		return position.add(offset, absPos);
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setOffset(float x, float y, float z) {
		this.offset.set(x, y, z);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}
	
	public void setPosition(Vector3f pos) {
		this.position.set(pos);
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
		rotation.rotateLocalZ(-z);
		rotation.normalize();
		forward.set(0,0,1).rotate(rotation);
		up.set(0, 1, 0).rotate(rotation);
	}
	
	public Vector3f getPointInFrontOf(float distance, Vector3f dest) {
		return forward.mul(distance, dest);
	}

}
