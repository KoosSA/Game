package app;

import org.lwjgl.glfw.GLFW;

import app.gui.Layers;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.utils.Globals;

public class MaterialEditor extends BaseGameLoop implements IInputHandler {

	public static void main(String[] args) {
		new MaterialEditor().start();
	}
	
	//public static RenderingDemoController controller;

	@Override
	protected void init() {
		//ResourceLoader.loadAllTextures();
		//ResourceLoader.loadAllModels();
		registerInputHandler(InputStates.GAME);
		registerInputHandler(InputStates.GUI);
		
		input.setInputReceiver(InputStates.GUI);
		
		camera.getPosition().set(0, 2, 5);
		FirstPersonCamera.class.cast(camera).pitch(-20);
		
//		Material mat = Registries.Models.getStaticModel("barrels_fbx.fbx").getMeshes().get(0).getMaterial();
//		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
//		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
//		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
//		mat.addTexture(TextureType.METALLIC, "drum3_metallic.png");
//		ModelManager.addModelInstanceToWorld(new ModelInstance(Registries.Models.getStaticModel("barrels_fbx.fbx")));
		
		gui.addGuiLayer("materialSettings", Layers.modelSettings);
		gui.show("materialSettings");
		
		physics.enableDebug();
		
	}

	@Override
	protected void update(float delta) {
		
	}

	@Override
	protected void render() {
		
	}
	
	@Override
	public void handleInputs(Input input, float delta) {
		if (input.isKeyJustPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
			if (Globals.input.getCurrentInputState().equals(InputStates.GAME)) {
				Globals.input.setInputReceiver(InputStates.GUI);
				gui.show("materialSettings");
			} else {
				Globals.input.setInputReceiver(InputStates.GAME);
				gui.hide("materialSettings");
			}
		}
	}

}
