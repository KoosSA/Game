package client;

import client.io.input.InputStates;
import client.io.input.receivers.handlers.IKeyInputHandler;
import client.logic.BaseGameLoop;

public class Client extends BaseGameLoop implements IKeyInputHandler {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		//Thread.sleep(5000);
		//c.disconnect();
		new Client().start();
	}

	@Override
	protected void init() {
		registerInputHandler(InputStates.GAME);
		//input.setInputReceiver(InputStates.NONE);
		//gui.loadXML("test.xml");
		//gui.loadXML("hud.xml");
		
		//gui.show("start");
		//gui.show("hud");
		
		//ModelLoader ml = new ModelLoader();
		
		//ml.loadModel("plane.fbx");
		input.setInputReceiver(InputStates.GAME);
	}

	@Override
	protected void update(float delta) {
		//HUDScreenController.update(delta);
		
	}

	@Override
	protected void render() {
		// TODO Auto-generated method stub
		
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

}
