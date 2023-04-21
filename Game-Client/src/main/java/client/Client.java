package client;

import client.logic.BaseGameLoop;

public class Client extends BaseGameLoop {
	
	public static void main(String[] args) {
		//Connection c = new Connection("Koos");
		//c.connect();
		
		//Thread.sleep(5000);
		
		//c.disconnect();
		
		new Client();
	}

	@Override
	protected void init() {
		
		gui.loadXML("test.xml");
		gui.loadXML("hud.xml");
		
		//gui.show("start");
		gui.show("hud");
		
		
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

}
