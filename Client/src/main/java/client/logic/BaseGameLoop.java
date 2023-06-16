package client.logic;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;

import client.gui.GuiTest;
import client.io.Window;
import client.io.input.Input;
import client.rendering.cameras.Camera;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.renderers.BaseRenderer;
import client.rendering.renderers.StaticRenderer;
import client.utils.registries.Registries;
import common.utils.timer.Timer;

public abstract class BaseGameLoop extends Thread {
	
	protected Input input;
	//protected Gui gui;
	protected BaseRenderer renderer;
	protected Camera camera;
	protected GuiTest gui;
	
	public BaseGameLoop() {
		setName("client_main");
	}
	
	@Override
	public void run() {
		Files.init("Resources", RootFileLocation.LOCAL);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		new Timer().start();
		new Window(this);
	}
	
	protected abstract void init();
	protected abstract void update(float delta);
	protected abstract void render();
	//protected abstract void onDispose();
	
	public void baseInit() {
		Log.debug(this, "Starting initialisation process.");
		//if (gui == null) gui = new Gui();
		if (gui == null) gui = new GuiTest();
		if (input == null) input = new Input();
		if (camera == null) camera = new FirstPersonCamera();
		if (renderer == null) renderer = new StaticRenderer(camera);
		
		init();
		Log.debug(this, "Initialisation of programm complete.");
	}
	
	public void baseUpdate(float delta) {
		InternalRegistries.update(delta);
		update(delta);
	}
	
	public void baseRender() {
		render();
		renderer.baseRender();
		gui.render();
	}
	
	
	public void dispose() {
		Log.debug(this, "Starting base loop disposal.");
		Timer.stopTimer();
		Registries.dispose();
		InternalRegistries.dispose();
		Log.debug(this, "Disposal of base loop finished. Saving log files....");
		Log.disposeAll();
	}

	public void baseResize(int width, int height) {
		InternalRegistries.onResize(width, height);
	}

	
	
}
