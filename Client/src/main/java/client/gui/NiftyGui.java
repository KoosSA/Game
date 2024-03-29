package client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.io.input.InputStates;
import client.logic.internalEvents.IDisposeHandler;
import client.logic.internalEvents.IResizeHandler;
import client.logic.internalEvents.IUpdateHandler;
import client.utils.Globals;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.batch.BatchRenderDevice;
import de.lessvoid.nifty.renderer.lwjgl3.input.Lwjgl3InputSystem;
import de.lessvoid.nifty.renderer.lwjgl3.render.Lwjgl3BatchRenderBackendCoreProfileFactory;
import de.lessvoid.nifty.renderer.lwjgl3.time.Lwjgl3TimeProvider;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;

public class NiftyGui implements IDisposeHandler, IUpdateHandler, IResizeHandler {
	
	public Nifty nifty;
	public Lwjgl3InputSystem input_system;
	public OpenALSoundDevice sound_device;
	public BatchRenderDevice render_device;
	public Lwjgl3TimeProvider time_provider;
	private List<String> filePaths;
	private String currentScreen;
	private String defaultScreen = "emptyScreen";
	private Screen defaultScreen2;
	private List<INiftyRestartEventSubscriber> restartSubscribers;
	
	public NiftyGui() {
		Log.debug(this, "Starting nifty igui initialisation.");
		registerUpdatable();
		registerDisposeHandler();
		registerResizeHandler();
		restartSubscribers = new ArrayList<INiftyRestartEventSubscriber>();
		Globals.ngui = this;
		input_system = new Lwjgl3InputSystem(Globals.window.getId());
		sound_device = new OpenALSoundDevice();
		//render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendFactory.create(Globals.window.getId()));
		render_device = new BatchRenderDevice(Lwjgl3BatchRenderBackendCoreProfileFactory.create(Globals.window.getId()));
		time_provider = new Lwjgl3TimeProvider();
		
		nifty = new Nifty(render_device, sound_device, input_system, time_provider);
		nifty.enableAutoScaling(Globals.window.getWidth(), Globals.window.getHeight());
		try {
			input_system.startup();
		} catch (Exception e) {
			Log.error(this, "Nifty input system startup failed.");
			e.printStackTrace();
		}
		//render_device.setDisplayFPS(true);
		filePaths = new ArrayList<>();
		defaultScreen2 = new Screen(nifty, "emptyScreen", new DefaultScreenController(), time_provider);
		nifty.addScreen(defaultScreen, defaultScreen2);
		show(defaultScreen);
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
	 * Default screen is initialised as "emptyScreen"
	 */
	public void hide() {
		show(defaultScreen);
		currentScreen = defaultScreen;
	}

	public void toggle(String id) {
		if (currentScreen.equals(id)) {
			hide();
		} else {
			show(id);
		}
	}
	
	public void setDefaultScreen(String id) {
		defaultScreen = id;
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
		Log.debug(this, "Trying to load nifty gui file named: " + fileName);
		String path = Files.getFolderPath("Gui") + "/" + fileName;
		try {
			nifty.validateXml(path);
		} catch (Exception e) {
			Log.error(this, "Invalid nifty gui xml file found: " + fileName);
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
		restartSubscribers.forEach(sub -> {
			sub.beforeRestart();
		});
		input_system.shutdown();
		nifty.getEventService().clearAllSubscribers();
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
		Globals.ngui.input_system = input_system;
		Globals.input.getInputReceiver(InputStates.GUI).reset();
		filePaths.forEach(path -> {
			nifty.addXml(path);
		});
		if (currentScreen != null) show(currentScreen);
		registerUpdatable();
		restartSubscribers.forEach(sub -> {
			sub.afterRestart();
		});
		Log.debug(this, "Nifty GUI sucsesfully restarted.");
	}

	protected void registerRestartSubscriber(INiftyRestartEventSubscriber iNiftyRestartEventSubscriber) {
		restartSubscribers.add(iNiftyRestartEventSubscriber);
	}

	

	

}
