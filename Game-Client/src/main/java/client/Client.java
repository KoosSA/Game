package client;

import client.io.input.InputStates;
import client.logic.BaseGameLoop;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.utils.registries.Registries;

public class Client extends BaseGameLoop {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		//Thread.sleep(5000);
		//c.disconnect();
		new Client().start();
	}

	@Override
	protected void init() {
		input.setInputReceiver(InputStates.GAME);
		gui.loadXML("renderDemoUI.xml");
//		gui.loadXML("hud.xml");
//		gui.loadXML("inv.xml");
		
		gui.show("renderDemoMainUI");
//		gui.show("hud");
		input.setInputReceiver(InputStates.GUI);
		
		//renderer = new StaticRenderer(new FirstPersonCamera());
		
		Model m = Registries.Models.getStaticModel("t.obj");
		m.getTransform().setScale(1f, 1, 1);
		m.getTransform().move(0, 0, -5f);
		m.getTransform().resetRotation();
		//m.getTransform().turn(20, 0, 0);
		
		Material mat = m.getMeshes().get(0).getMaterial();
		//mat.setDiffuseColour(1, 1, 1, 1);
		//mat.removeTexture(TextureType.DIFFUSE);
		mat.addTexture(TextureType.DIFFUSE, "drum3_base_color.png");
		mat.addTexture(TextureType.NORMAL, "drum3_normal.png");
		mat.addTexture(TextureType.SPECULAR, "drum3_roughness.png");
		//mat.addTexture(TextureType.DISPLACEMENT, "brick_disp.png");
//		Registries.Models.getStaticModel("uc_uv_sphere.fbx").getTransform().setScale(0.5f, 0.5f, 1f);
//		Registries.Models.getStaticModel("uc_uv_sphere.fbx").getTransform().move(0, 0, 5f);
		
		//mat.addTexture(TextureType.DIFFUSE, "brick_diff.jpg");
		//mat.addTexture(TextureType.NORMAL, "brick_normal.jpg");
		
		//Registries.Lights.getAmbientLight().setIntensity(0.1f);
		//Registries.Lights.getDirectionalLight().setDirection(-0.5f, -0.5f, 0);
		
		
		//GLFW.glfwSetInputMode(Globals.window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

	
	@Override
	protected void update(float delta) {
		//HUDScreenController.update(delta);
		//Registries.Models.getStaticModel("monkey_ut.fbx").getTransform().turn(0,1,0);
		//Registries.Models.getStaticModel("t.fbx").getTransform().move(-0.1f, 0, -0);
		//Registries.Models.getStaticModel("t.fbx").getTransform().getRotation().identity();
		//Registries.Models.getStaticModel("t.obj").getTransform().setScale(1f, 1f, 1f);
		//Registries.Models.getStaticModel("barrels_fbx.fbx").getTransform().turn(0, 1, 0);
		
	}

	@Override
	protected void render() {
		//renderer.baseRender();
	}

}
