package client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.io.input.InputStates;
import client.logic.internalEvents.IDisposable;
import client.logic.internalEvents.IResizable;
import client.logic.internalEvents.IUpdatable;
import client.utils.Globals;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;
import de.lessvoid.nifty.renderer.lwjgl3.render.Lwjgl3BatchRenderBackendCoreProfileFactory;
import de.lessvoid.nifty.renderer.lwjgl3.time.Lwjgl3TimeProvider;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;

public class NiftyGui implements IDisposable, IUpdatable, IResizable {
	
	public Nifty nifty;
	public Lwjgl3InputSystem input_system;
	public OpenALSoundDevice sound_device;
	public BatchRenderDevice render_device;
	public Lwjgl3TimeProvider time_provider;
	private List<String> filePaths;
	private String currentScreen;
	private String defaultScreen = "hud";
	
	public NiftyGui() {
		Log.debug(this, "Starting nifty gui initialisation.");
		registerUpdatable();
		registerDisposeHandler();
		registerResizeHandler();
//		Globals.gui = this;
		input_system = new Lwjgl3InputSystem(Globals.window.getId());
		sound_device = new OpenALSoundDevice();
		//render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendFactory.create(Globals.window.getId()));
		render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendCoreProfileFactory.create(Globals.window.getId()));
		time_provider = new Lwjgl3TimeProvider();
		try {
			input_system.startup();
		} catch (Exception e) {
			Log.error(this, "Nifty input system startup failed.");
			e.printStackTrace();
		}
		nifty = new Nifty(render_device, sound_device, input_system, time_provider);
		nifty.enableAutoScaling(Globals.window.getWidth(), Globals.window.getHeight());
		//render_device.setDisplayFPS(true);
		filePaths = new ArrayList<>();
		Log.debug(this, "NiftyGui system initialised.");
	}
	
	public void renderDebugFPS(boolean shouldRender) {
		render_device.setDisplayFPS(shouldRender);
	}
	
	public void show(String id) {
		Log.debug(this, "NiftyGui switching to screen: " + id);
		nifty.gotoScreen(id);
		currentScreen = id;
	}
	
	/**
	 * Switches to the default screen.
	 * Default screen is initialised as "hud"
	 */
	public void close() {
		nifty.gotoScreen(defaultScreen);
		currentScreen = defaultScreen;
	}

	
	@Override
	public void update(float delta) {
		nifty.update();
	}
	
	public void render() {
		GL30.glDisable(GL30.GL_DEPTH_TEST);
		nifty.render(false);
		GL30.glEnable(GL30.GL_DEPTH_TEST);
	}
	
	public void dispose() {
		Log.debug(this, "Starting NiftyGui disposal");
		input_system.shutdown();
		nifty.exit();
		unRegisterUpdatable();
		Log.debug(this, "NiftyGui disposal finished.");
	}
	
	public void loadXML(String fileName) {
		Log.debug(this, "Trying to load gui file named: " + fileName);
		String path = Files.getFolderPath("NiftyGui") + "/" + fileName;
		try {
			nifty.validateXml(path);
		} catch (Exception e) {
			Log.error(this, "Invalid gui xml file found: " + fileName);
			e.printStackTrace();
		}
		nifty.addXml(path);
		if (!filePaths.contains(path)) filePaths.add(path);
		Log.debug(this, "Loading NiftyGui finished: " + fileName);
	}
	
	
	@Override
	public void onResize(int width, int height) {
		Log.debug(this, "Initialising restart of GUI systems due to window resize event.");
		restart();
	}
	
	private void restart() {
		Log.debug(this, "Restarting GUI system.");
		unRegisterUpdatable();
		input_system.shutdown();
		nifty.exit();
		Globals.input.getInputReceiver(InputStates.GUI).dispose();
		
		input_system = new Lwjgl3InputSystem(Globals.window.getId());
		render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendCoreProfileFactory.create(Globals.window.getId()));
		nifty = new Nifty(render_device, sound_device, input_system, time_provider);
		try {
			input_system.startup();
		} catch (Exception e) {
			Log.error(this, "Nifty input system startup failed.");
			e.printStackTrace();
		}
		nifty.enableAutoScaling(Globals.window.getWidth(), Globals.window.getHeight());
//		Globals.gui.input_system = input_system;
		Globals.input.getInputReceiver(InputStates.GUI).reset();
		
		filePaths.forEach(path -> {
			nifty.addXml(path);
		});
		if (currentScreen != null) show(currentScreen);
		registerUpdatable();
		Log.debug(this, "GUI sucsesfully restarted.");
	}

	

	

}
