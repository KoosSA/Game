package app;

import app.renderers.MaterialRenderer;
import client.io.input.InputStates;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.utils.Globals;
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
		
		input.setInputReceiver(InputStates.GUI);
		
		gui.loadXML("renderDemoUI.xml");
		gui.show("renderDemoMainUI");
		
		renderer = new MaterialRenderer(camera);
		
		camera.getPosition().set(0, 2, 5);
		FirstPersonCamera.class.cast(camera).pitch(-20);
		
		Material mat = Registries.Models.getStaticModel("barrels_fbx.fbx").getMeshes().get(0).getMaterial();
		mat.addTexture(TextureType.BASE_COLOUR, "drum3_base_color.png");
		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat.addTexture(TextureType.ROUGHNESS, "drum3_roughness.png");
		mat.addTexture(TextureType.METALLIC, "drum3_metallic.png");
		
		MaterialRenderer.class.cast(renderer).setModel(Registries.Models.getStaticModel("barrels_fbx.fbx"));
		
	}

	@Override
	protected void update(float delta) {
		
	}

	@Override
	protected void render() {
		
	}

}
