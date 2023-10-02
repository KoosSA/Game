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

	@Override
	protected void init() {
		registerInputHandler(InputStates.GAME);
		registerInputHandler(InputStates.GUI);
		
		input.setInputReceiver(InputStates.GUI);
		
		camera.getPosition().set(0, 2, 5);
		FirstPersonCamera.class.cast(camera).pitch(-20);
		
		igui.addGuiLayer("materialSettings", Layers.modelSettings);
		igui.show("materialSettings");
		
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
				igui.show("materialSettings");
			} else {
				Globals.input.setInputReceiver(InputStates.GAME);
				igui.hide("materialSettings");
			}
		}
	}

}
