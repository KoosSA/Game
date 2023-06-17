package client.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import client.rendering.utils.Transform;

public class MathUtil {
	
	private static Matrix4f tempMat4 = new Matrix4f();
	private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	private static Vector3f tempVec3 = new Vector3f();

	public static float clamp(float value, float min, float max) {
		if (value > max) value = max;
		if (value < min) value = min;
		return value;
	}
	
	public static float loop(float value, float min, float max) {
		if (value >= max) value = min + (value - max);
		if (value <= min) value = max - (min - value);
		return value;
	}
	
	public static float[] listToArrayFloat(List<Float> list) {
		float[] arr = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	public static int[] ListToArrayInteger(List<Integer> list) {
		int[] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	public static Matrix4f getTransformationMatrix(Transform transform) {
		tempMat4.identity();
		tempMat4.translate(transform.getPosition());
		tempMat4.rotate(transform.getRotation());
		tempMat4.scale(transform.getScale());
		return tempMat4;
	}
	
	public static Matrix4f getTransformationMatrix(Vector3f position, Quaternionf rotation, Vector3f scale) {
		tempMat4.identity();
		tempMat4.translate(position);
		tempMat4.rotate(rotation);
		tempMat4.scale(scale);
		return tempMat4;
	}
	
	public static Matrix4f getViewMatrix(Vector3f position, float pitch, float yaw, Vector3f scale) {
		tempMat4.identity();
		tempMat4.scale(scale);
		tempMat4.setRotationXYZ(pitch, yaw , 0);
		tempMat4.translate(position.negate(tempVec3));
		
		return tempMat4;
	}

	public static Matrix4f get3DProjectionMatrix(float near_plane, float far_plane, float fovy) {
		tempMat4.identity();
		tempMat4.setPerspective(Math.toRadians(fovy), getAspectRatio(Globals.window.getWidth(), Globals.window.getHeight()), near_plane, far_plane);
		return tempMat4;
	}
	
	public static float getAspectRatio(float width, float height) {
		return width / height;
	}

	public static FloatBuffer matrix4fToFloatBuffer(Matrix4f matrix4f) {
		matrix4f.get(matrix4fBuffer);
		return matrix4fBuffer;
	}
	
	public static FloatBuffer floatArrayToBuffer(float[] data) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(data.length);
		buff.put(data);
		buff.flip();
		return buff;
	}
	
	public static IntBuffer intArrayToBuffer(int[] data) {
		IntBuffer buff = BufferUtils.createIntBuffer(data.length);
		buff.put(data);
		buff.flip();
		return buff;
	}

	
	
	

}
