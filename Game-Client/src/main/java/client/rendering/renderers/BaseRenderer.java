package client.rendering.renderers;

import client.rendering.cameras.Camera;

public abstract class BaseRenderer {
	
	protected Camera cam;
	
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
