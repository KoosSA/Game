package app;

import app.renderers.MaterialRenderer;
import client.io.input.InputStates;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.utils.ResourceLoader;
import client.utils.registries.Registries;

public class MaterialEditor extends BaseGameLoop {

	public static void main(String[] args) {
		new MaterialEditor().start();
	}

	@Override
	protected void init() {
		ResourceLoader.loadAllTextures();
		ResourceLoader.loadAllModels();
		
		gui.loadXML("renderDemoUI.xml");
		gui.show("renderDemoMainUI");
		
		input.setInputReceiver(InputStates.GUI);
		
		renderer = new MaterialRenderer(camera);
		
		camera.getPosition().set(0, 2, 5);
		FirstPersonCamera.class.cast(camera).pitch(-20);
		
	}

	@Override
	protected void update(float delta) {
		
	}

	@Override
	protected void render() {
		
	}

}
