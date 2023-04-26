package client.rendering.utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import client.utils.MathUtil;

public class Transform {

	private Vector3f position = new Vector3f();
	private Vector3f offset = new Vector3f();
	private Vector3f scale = new Vector3f();
	private Quaternionf rotation = new Quaternionf();
	private static Vector3f absPos = new Vector3f();

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

	public void setRotation(Quaternionf rotation) {
		this.rotation = rotation;
	}

	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}
}
