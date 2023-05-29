package client.rendering.renderers;

import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IResizable;
import client.rendering.cameras.Camera;
import client.rendering.shaders.BaseShader;

public abstract class BaseRenderer implements IResizable, IDisposable {
	
	protected Camera cam;
	protected BaseShader shader;
	
	public BaseRenderer(Camera cam) {
		this.cam = cam;
		registerResizeHandler();
		registerDisposeHandler();
	}
	
	public void baseRender() {
		render();
	}
	
	protected abstract void render();

	
	

}
