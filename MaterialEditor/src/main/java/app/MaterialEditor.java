package app;

import client.io.input.InputStates;
import client.logic.BaseGameLoop;

public class MaterialEditor extends BaseGameLoop {

	public static void main(String[] args) {
		new MaterialEditor().start();
	}

	@Override
	protected void init() {
		gui.loadXML("renderDemoUI.xml");
		gui.show("renderDemoMainUI");
		input.setInputReceiver(InputStates.GUI);
	}

	@Override
	protected void update(float delta) {
		
	}

	@Override
	protected void render() {
		
	}

}
