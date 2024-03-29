package client.logic;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import client.gui.ImGuiImpl;
import client.gui.NiftyGui;
import client.io.Window;
import client.io.input.Input;
import client.physics.Physics;
import client.rendering.cameras.Camera;
import client.rendering.cameras.FirstPersonCamera;
import client.rendering.renderers.BaseRenderer;
import client.rendering.renderers.StaticRenderer;
import client.utils.registries.Registries;
import common.utils.timer.Timer;

public abstract class BaseGameLoop extends Thread {
	
	protected Input input;
	protected BaseRenderer renderer;
	protected Camera camera;
	protected ImGuiImpl igui;
	protected NiftyGui ngui;
	protected Physics physics;
	
	public BaseGameLoop() {
		setName("client_main");
	}
	
	@Override
	public void run() {
		Files.init("Resources", RootFileLocation.LOCAL);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		SaveSystem.init(Files.getCommonFolder(CommonFolders.Saves), Files.getFolder("Data"));
		new Timer(100).start();
		new Window(this);
	}
	
	protected abstract void init();
	protected abstract void update(float delta);
	protected abstract void render();
	//protected abstract void onDispose();
	
	public void baseInit() {
		Log.debug(this, "Starting initialisation process.");
		if (ngui == null) ngui = new NiftyGui();
		if (igui == null) igui = new ImGuiImpl();
		if (input == null) input = new Input();
		if (camera == null) camera = new FirstPersonCamera();
		if (physics == null) physics = new Physics(camera);
		if (renderer == null) renderer = new StaticRenderer(camera);
		
		init();
		Log.debug(this, "Initialisation of programm complete.");
	}
	
	public void baseUpdate(float delta) {
		InternalEventHandlerRegistry.update(delta);
		update(delta);
	}
	
	public void baseRender() {
		render();
		renderer.baseRender();
		physics.debugDraw();
		ngui.render();
		//igui.render();
	}
	
	
	public void dispose() {
		Log.debug(this, "Starting base loop disposal.");
		Timer.stopTimer();
		Registries.dispose();
		InternalEventHandlerRegistry.dispose();
		Log.debug(this, "Disposal of base loop finished. Saving log files....");
		Log.disposeAll();
	}

	public void baseResize(int width, int height) {
		InternalEventHandlerRegistry.onResize(width, height);
	}

	
	
}
