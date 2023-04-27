package client.logic;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;

import client.gui.Gui;
import client.io.Window;
import client.io.input.Input;
import client.utils.Registries;

public abstract class BaseGameLoop extends Thread {
	
	protected Input input;
	protected Gui gui;
	
	public BaseGameLoop() {
		setName("client_main");
	}
	
	@Override
	public void run() {
		Files.init("Resources", RootFileLocation.LOCAL);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		new Window(this);
	}
	
	protected abstract void init();
	protected abstract void update(float delta);
	protected abstract void render();
	//protected abstract void onDispose();
	protected abstract void resize(int width, int height);
	
	public void baseInit() {
		Log.debug(this, "Starting initialisation process.");
		if (gui == null) gui = new Gui();
		if (input == null) input = new Input();
		
		init();
		Log.debug(this, "Initialisation of programm complete.");
	}
	
	public void baseUpdate(float delta) {
		gui.update();
		
		update(delta);
	}
	
	public void baseRender() {
		render();
		gui.render();
	}
	
	
	public void dispose() {
		Log.debug(this, "Starting base loop disposal.");
		gui.dispose();
		input.dispose();
		Registries.dispose();
		Log.debug(this, "Disposal of base loop finished. Saving log files....");
		Log.disposeAll();
	}

	public void baseResize(int width, int height) {
		if (gui != null) gui.resize(width, height);
		resize(width, height);
	}

	
	
}
