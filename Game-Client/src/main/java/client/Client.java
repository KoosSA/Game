package client;

import client.io.input.InputStates;
import client.logic.BaseGameLoop;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Model;
import client.rendering.renderers.StaticRenderer;
import client.utils.registries.Registries;

public class Client extends BaseGameLoop {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		//Thread.sleep(5000);
		//c.disconnect();
		new Client().start();
	}
	
	
	private StaticRenderer renderer;

	@Override
	protected void init() {
		//input.setInputReceiver(InputStates.NONE);
//		gui.loadXML("test.xml");
//		gui.loadXML("hud.xml");
//		gui.loadXML("inv.xml");
		
		
		input.setInputReceiver(InputStates.GAME);
		
		renderer = new StaticRenderer(new FirstPersonCamera());
		
		Model m = Registries.Models.getStaticModel("box.fbx");
		m.getTransform().setScale(1f, 1, 1);
		m.getTransform().move(0, 0, -5f);
		m.getTransform().resetRotation();
		//m.getTransform().turn(20, 0, 0);
		
		Material mat = m.getMeshes().get(0).getMaterial();
		//mat.setDiffuseColour(1, 0, 0, 1);
		//mat.setUseTexture(false);
		mat.addTexture(TextureType.DIFFUSE, "brick_diff.jpg");
		mat.addTexture(TextureType.NORMAL, "brick_normal.jpg");
		mat.addTexture(TextureType.SPECULAR, "brick_spec.jpg");
		//mat.addTexture(TextureType.DISPLACEMENT, "brick_disp.png");
//		Registries.Models.getStaticModel("uc_uv_sphere.fbx").getTransform().setScale(0.5f, 0.5f, 1f);
//		Registries.Models.getStaticModel("uc_uv_sphere.fbx").getTransform().move(0, 0, 5f);
		
		Registries.Lights.getAmbientLight().setIntensity(0.5f);
	}

	@Override
	protected void update(float delta) {
		//HUDScreenController.update(delta);
		//Registries.Models.getStaticModel("monkey_ut.fbx").getTransform().turn(0,1,0);
		//Registries.Models.getStaticModel("t.fbx").getTransform().move(-0.1f, 0, -0);
		//Registries.Models.getStaticModel("t.fbx").getTransform().getRotation().identity();
//		Registries.Models.getStaticModel("tsc.fbx").getTransform().setScale(1f, 1f, 1f);
	}

	@Override
	protected void render() {
		renderer.baseRender();
	}

}
