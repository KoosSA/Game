package client;

import client.io.input.InputStates;
import client.utils.Globals;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class StartScreenController implements ScreenController {
	
	public void quit() {
		
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
	}

	@Override
	public void onStartScreen() {
		Globals.input.setInputReceiver(InputStates.GUI);
	}

	@Override
	public void onEndScreen() {
		Globals.input.setInputReceiver(InputStates.NONE);
	}

}
