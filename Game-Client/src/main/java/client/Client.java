package client;

import client.io.input.InputStates;
import client.io.input.receivers.handlers.IKeyInputHandler;
import client.logic.BaseGameLoop;
import client.rendering.renderers.StaticRenderer;
import client.rendering.utils.RenderManager;
import client.utils.Registries;

public class Client extends BaseGameLoop implements IKeyInputHandler {
	
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
		registerInputHandler(InputStates.GAME);
		//input.setInputReceiver(InputStates.NONE);
		gui.loadXML("test.xml");
		gui.loadXML("hud.xml");
		gui.loadXML("inv.xml");
		
		//gui.show("start");
		//gui.show("hud");
		gui.show("inv");
		
		//ModelLoader ml = new ModelLoader();
		
		//ml.loadModel("plane.fbx");
		input.setInputReceiver(InputStates.GAME);
		
		renderer = new StaticRenderer(null);
		
		RenderManager.getStaticModels().add(Registries.Models.getModel("plane.fbx"));
		Registries.Models.getModel("plane.fbx").getTransform().setScale(1000, 1000, 1);
	}

	@Override
	protected void update(float delta) {
		//HUDScreenController.update(delta);
		
	}

	@Override
	protected void render() {
		renderer.baseRender();
	}

	@Override
	protected void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyPress(int key, int mods) {
		System.out.println("Key press: " + key);
	}

	@Override
	public void onKeyDown(int key, int mods) {
		System.out.println("Key down: " + key);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		renderer.baseDispose();
	}

}
