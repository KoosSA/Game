package client.rendering.renderers;

import client.logic.internalEvents.IInternalEventDispose;
import client.logic.internalEvents.IInternalEventResize;
import client.rendering.cameras.Camera;
import client.rendering.shaders.BaseShader;

public abstract class BaseRenderer implements IInternalEventResize, IInternalEventDispose {
	
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
