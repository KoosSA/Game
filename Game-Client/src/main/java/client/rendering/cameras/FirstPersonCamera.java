package client.rendering.cameras;

import org.joml.Matrix4f;

import client.rendering.utils.RenderMaths;

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
		return RenderMaths.get3DViewMatrix(fovy, near_plane, far_plane);
	}
	
	

}
