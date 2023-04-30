package client.rendering.renderers;

import client.rendering.cameras.Camera;
import client.rendering.shaders.BaseShader;

public abstract class BaseRenderer {
	
	protected Camera cam;
	protected BaseShader shader;
	
	public BaseRenderer(Camera cam) {
		this.cam = cam;
	}
	
	public void baseRender() {
		render();
	}
	
	public void baseDispose() {
		dispose();
	}
	
	protected abstract void render();
	
	protected abstract void dispose();

	
	

}
