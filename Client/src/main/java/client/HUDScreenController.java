package client;

import com.koossa.logger.Log;

import client.logic.internalEvents.IUpdatable;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HUDScreenController implements ScreenController, IUpdatable {
	
	static Label renderFPS;
	static Label updateFPS;
	static Label clientCounter;
	static Screen screen;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		Log.debug(this, "Binding hud screen.");
		renderFPS = screen.findNiftyControl("fps_counter", Label.class);
		updateFPS = screen.findNiftyControl("update_counter", Label.class);
		clientCounter = screen.findNiftyControl("connected_clients", Label.class);
		HUDScreenController.screen = screen;
		
		
	}
	
	@Override
	public void update(float delta) {
		renderFPS.setText("TestTextSystem render FPS: " + (int) (1/delta));
	}

	@Override
	public void onStartScreen() {
		Log.debug(this, "Starting hud screen.");
		registerUpdatable();
	}

	@Override
	public void onEndScreen() {
		Log.debug(this, "Ending hud screen.");
		unRegisterUpdatable();
	}

}
