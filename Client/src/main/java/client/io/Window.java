package client.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import client.logic.BaseGameLoop;
import client.utils.Globals;

/**
 * Handles display window creation and settings.
 * @author Koos
 *
 */
public class Window {
	
	private int width = 900;
	private int height = 700;
	private boolean FULLSCREEN = false;
	private boolean VSYNC = false;
	private String TITLE = "Engine";
	private long id;
	private float TARGET_FPS = 60;
	private boolean initialised = false;
	private GLFWVidMode videoMode;
	private long primaryMonitor;
	private BaseGameLoop gameloop;
	private boolean resizing = false;
	private int previousWH;
	private float deltaTime = 0;
	
	public Window(BaseGameLoop gameLoop) {
		Globals.window = this;
		this.gameloop = gameLoop;
		createWindow();
	}
	
	/**
	 * Creates the display window.
	 * Can only be called once on project startup.
	 * @param client 
	 */
	private void createWindow() {
		if (initialised) return;
		Log.info(Window.class, "Creating the main display window.");
		
		
		GLFW.glfwDefaultWindowHints();
		//GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		
		if (!GLFW.glfwInit()) {
			Log.error(Window.class, "Failed to create GLFW context - Aborting...");
			return;
		}
		primaryMonitor = GLFW.glfwGetPrimaryMonitor();
		videoMode = GLFW.glfwGetVideoMode(primaryMonitor);
		id = GLFW.glfwCreateWindow(width, height, TITLE, 0, 0);
		GLFW.glfwMakeContextCurrent(id);
		GL.createCapabilities();
		
		Log.info(this, "OpenGL initialised with version: " + GL11.glGetString(GL11.GL_VERSION));
		GL30.glViewport(0, 0, width, height);
		GLFW.glfwSetWindowSizeCallback(id, sizeCallBack);
		if (FULLSCREEN) makeFullscreen();
		if (VSYNC) {
			GLFW.glfwSwapInterval(1);
		}
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(id);
		Log.info(Window.class, "Display created.");
		gameloop.baseInit();
		loop();
		dispose();
	}
	
	private void loop() {
		long previousTime = (long) System.nanoTime();
		float targetTime =  (1.000f / TARGET_FPS);
		if (TARGET_FPS > 0) {
			while (!GLFW.glfwWindowShouldClose(id)) {
				deltaTime = (float) (System.nanoTime() - previousTime) / 1000000000.00000f;
				if (deltaTime >= targetTime) {
					previousTime = System.nanoTime();
					update(deltaTime);
					render();
					
				} else {
					try {
						Thread.sleep((long) ((targetTime - deltaTime) * 1000));
					} catch (InterruptedException e) {
						Log.error(Window.class, "Thread failed to sleep.");
						e.printStackTrace();
					}
				}
			} 
		} else {
			while (!GLFW.glfwWindowShouldClose(id)) {
				deltaTime = (float) (System.nanoTime() - previousTime) / 1000000000.00000f;
				previousTime = System.nanoTime();
				update(deltaTime);
				render();
			} 
		}
	}
	
	private void update(float delta) {
		if (resizing) {
			if (previousWH == width + height) {
				resizing = false;
				resize(width, height);
			}
		}
		previousWH = width + height;
		gameloop.baseUpdate(delta);
		GLFW.glfwPollEvents();
	}
	
	private void render() {
		GL30.glClearColor(0.25f, 0.25f, 0.25f, 1);
		GL30.glClear(GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_COLOR_BUFFER_BIT | GL30.GL_STENCIL_BUFFER_BIT);
		gameloop.baseRender();
		GLFW.glfwSwapBuffers(id);
	}
	
	/**
	 * Set the application to fullscreen mode.
	 */
	public void makeFullscreen() {
		width = videoMode.width();
		height = videoMode.height();
		GLFW.glfwSetWindowMonitor(id, primaryMonitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
		GL30.glViewport(0, 0, width, height);
	}
	
	private void dispose() {
		Log.debug(this, "Window disposing.");
		sizeCallBack.free();
		GLFW.glfwDestroyWindow(id);
		GLFW.glfwTerminate();
		gameloop.dispose();
	}
	
	private GLFWWindowSizeCallback sizeCallBack = new GLFWWindowSizeCallback() {
		@Override
		public void invoke(long window, int w, int h) {
			resizing = true;
			width = w;
			height = h;
		}
	};
	
	private void resize(int w, int h) {
		Log.debug(this, "Resizing window to: " + w + " x " + h);
		GL30.glViewport(0, 0, w, h);
		gameloop.baseResize(w, h);
	}
	
	/**
	 * Tells the application that it should close on the next update cycle.
	 */
	public void exit() {
		GLFW.glfwSetWindowShouldClose(id, true);
	}

	/**
	 * Gets the current title of the application
	 * @return title
	 */
	public String getTITLE() {
		return TITLE;
	}

	/**
	 * Sets the title of the application
	 * @param tITLE
	 */
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	/**
	 * Gets the current width of the application window
	 * @return windowWidth
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the current height of the application window
	 * @return windowHeight
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the application window's id
	 * @return
	 */
	public long getId() {
		return id;
	}

}
