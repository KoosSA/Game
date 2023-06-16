package app;

import org.lwjgl.glfw.GLFW;

import app.gui.Layers;
import app.gui.controllers.RenderingDemoController;
import app.renderers.MaterialRenderer;
import client.gui.IGuiLayer;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.utils.Globals;
import client.utils.ResourceLoader;
import client.utils.registries.Registries;
import imgui.ImGui;

public class MaterialEditor extends BaseGameLoop implements IInputHandler {

	public static void main(String[] args) {
		new MaterialEditor().start();
	}
	
	public static RenderingDemoController controller;

	@Override
	protected void init() {
		//ResourceLoader.loadAllTextures();
		ResourceLoader.loadAllModels();
		registerInputHandler(InputStates.GAME);
		registerInputHandler(InputStates.GUI);
		
		input.setInputReceiver(InputStates.GUI);
		
//		gui.loadXML("renderDemoUI.xml");
//		gui.show("renderDemoMainUI");
		
		renderer = new MaterialRenderer(camera);
		
		camera.getPosition().set(0, 2, 5);
		FirstPersonCamera.class.cast(camera).pitch(-20);
		
		Material mat = Registries.Models.getStaticModel("barrels_fbx.fbx").getMeshes().get(0).getMaterial();
//		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
//		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
//		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
//		mat.addTexture(TextureType.METALLIC, "drum3_metallic.png");
		
		MaterialRenderer.class.cast(renderer).setModel(Registries.Models.getStaticModel("barrels_fbx.fbx"));
		
		
		gui.addGuiLayer(Layers.modelSettings);
		
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
			} else {
				Globals.input.setInputReceiver(InputStates.GAME);
			}
			//controller.getInputState().setText(Globals.input.getCurrentInputState().name());
		}
	}

}
