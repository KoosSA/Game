package client.gui;

import java.util.ArrayList;
import java.util.List;

import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.io.input.InputStates;
import client.utils.Globals;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;
import de.lessvoid.nifty.renderer.lwjgl3.render.Lwjgl3BatchRenderBackendFactory;
import de.lessvoid.nifty.renderer.lwjgl3.time.Lwjgl3TimeProvider;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;

public class Gui {
	
	public Nifty nifty;
	public Lwjgl3InputSystem input_system;
	public OpenALSoundDevice sound_device;
	public BatchRenderDevice render_device;
	public Lwjgl3TimeProvider time_provider;
	private List<String> filePaths;
	private String currentScreen;
	
	public Gui() {
		Globals.gui = this;
		input_system = new Lwjgl3InputSystem(Globals.window.getId());
		sound_device = new OpenALSoundDevice();
		render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendFactory.create(Globals.window.getId()));
		time_provider = new Lwjgl3TimeProvider();
		try {
			input_system.startup();
		} catch (Exception e) {
			Log.error(this, "Nifty input system startup failed.");
			e.printStackTrace();
		}
		nifty = new Nifty(render_device, sound_device, input_system, time_provider);
		nifty.enableAutoScaling(Globals.window.getWidth(), Globals.window.getHeight());
		render_device.setDisplayFPS(true);
		filePaths = new ArrayList<>();
	}
	
	public void show(String id) {
		Log.debug(this, "Gui switching to screen: " + id);
		nifty.gotoScreen(id);
		currentScreen = id;
	}
	
	public void close() {
		nifty.gotoScreen("hud");
		currentScreen = "hud";
	}

	public void update() {
		nifty.update();
	}
	
	public void render() {
		nifty.render(false);
	}
	
	public void dispose() {
		Log.debug(this, "Disposing gui and closing niftyGui.");
		input_system.shutdown();
		nifty.exit();
	}
	
	public void loadXML(String fileName) {
		String path = Files.getFolderPath("Gui") + "/" + fileName;
		try {
			nifty.validateXml(path);
		} catch (Exception e) {
			Log.error(this, "Invalid gui xml file found: " + fileName);
			e.printStackTrace();
		}
		nifty.addXml(path);
		if (!filePaths.contains(path)) filePaths.add(path);
	}
	
	public void resize(int width, int height) {
		restart();
	}
	
	private void restart() {
		Log.debug(this, "Restarting GUI system.");
		input_system.shutdown();
		nifty.exit();
		
		input_system = new Lwjgl3InputSystem(Globals.window.getId());
		render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendFactory.create(Globals.window.getId()));
		nifty = new Nifty(render_device, sound_device, input_system, time_provider);
		try {
			input_system.startup();
		} catch (Exception e) {
			Log.error(this, "Nifty input system startup failed.");
			e.printStackTrace();
		}
		nifty.enableAutoScaling(Globals.window.getWidth(), Globals.window.getHeight());
		render_device.setDisplayFPS(true);
		Globals.input.getInputReceiver(InputStates.GUI).reset();
		
		filePaths.forEach(path -> {
			nifty.addXml(path);
		});
		if (currentScreen != null) show(currentScreen);
	}

}
