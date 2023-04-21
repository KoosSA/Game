package client.io.input;

import java.util.HashMap;
import java.util.Map;

import com.koossa.logger.Log;

import client.io.input.receivers.DefaultInputReceiver;
import client.io.input.receivers.GuiInputReceiver;
import client.utils.Globals;

public class Input {
	
	private Map<InputStates, InputReceiver> receivers = new HashMap<InputStates, InputReceiver>();
	private InputReceiver currentReceiver;
	private DefaultInputReceiver defaultReceiver = new DefaultInputReceiver();
	
	public Input() {
		Globals.input = this;
		registerInputReceiver(InputStates.NONE, defaultReceiver);
		registerInputReceiver(InputStates.GUI, new GuiInputReceiver());
		
		setInputReceiver(InputStates.NONE);
	}
	
	public void registerInputReceiver(InputStates state, InputReceiver receiver) {
		Log.debug(this, "Adding input receiver (" + receiver + ") to state: " + state);
		receivers.putIfAbsent(state, receiver);
	}

	public void setInputReceiver(InputStates state) {
		Log.debug(this, "Changing input receivers to: " + state);
		currentReceiver = receivers.getOrDefault(state, defaultReceiver);
		if (currentReceiver != null) currentReceiver.activate();
	}
	
	public InputReceiver getInputReceiver(InputStates state) {
		return receivers.getOrDefault(state, defaultReceiver);
	}
	
	public void update(float delta) {
		
	}

	public void dispose() {
		receivers.values().forEach(rec -> {
			rec.dispose();
		});
	}
}
