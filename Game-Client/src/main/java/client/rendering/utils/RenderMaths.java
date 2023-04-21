package client.rendering.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import client.utils.Globals;

public class RenderMaths {
	
	private static Matrix4f tempMat4 = new Matrix4f();
	private static Vector3f tempVec3 = new Vector3f();
	
	public static Matrix4f getTransformationMatrix(Transform transform) {
		tempMat4.identity();
		tempMat4.scale(transform.getScale());
		tempMat4.rotate(transform.getRotation());
		tempMat4.translate(transform.getAbsolutePosition(tempVec3));
		return tempMat4;
	}

	public static Matrix4f get3DViewMatrix(float near_plane, float far_plane, float fovy) {
		tempMat4.identity();
		tempMat4.perspective(fovy, getAspectRatio(Globals.window.getWidth(), Globals.window.getHeight()), near_plane, far_plane);
		
		return tempMat4;
	}
	
	public static float getAspectRatio(float width, float height) {
		return width / height;
	}

}
