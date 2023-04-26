package client.rendering.cameras;

import org.joml.Matrix4f;

import client.utils.MathUtil;

public class FirstPersonCamera extends Camera {
	
	private float fovy = 50f;
	private float near_plane = 0.00001f;
	private float far_plane = 1000f;
	
	public FirstPersonCamera(float fovy, float near_plane, float far_plane) {
		this.fovy = fovy;
		this.near_plane = near_plane;
		this.far_plane = far_plane;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return MathUtil.get3DProjectionMatrix(near_plane, far_plane, fovy);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return MathUtil.get3DProjectionMatrix(near_plane, far_plane, fovy);
	}
	
	

}
