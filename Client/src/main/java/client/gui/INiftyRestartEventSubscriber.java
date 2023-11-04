package client.gui;

import client.utils.Globals;

public interface INiftyRestartEventSubscriber {
	
	default void afterRestart() {
		
	}
	
	default void beforeRestart() {
		
	}
	
	default void registerRestartEventSubscriber() {
		Globals.ngui.registerRestartSubscriber(this);
	}

}
