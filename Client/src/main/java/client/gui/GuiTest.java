package client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import client.logic.internalEvents.IDisposable;
import client.utils.Globals;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class GuiTest implements IDisposable{
	
	private ImGuiImplGlfw glfwImpl;
	private ImGuiImplGl3 glImpl;
	private ImGuiIO io;
	private List<IGuiLayer> layers;
	
	public GuiTest() {
		registerDisposeHandler();
		ImGui.createContext();
		io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
		glfwImpl = new ImGuiImplGlfw();
		glImpl = new ImGuiImplGl3();
		glfwImpl.init(Globals.window.getId(), false);
		glImpl.init("#version 130");
		Globals.gui = this;
		layers = new ArrayList<>();
		
	}
	
	public void render() {
		glfwImpl.newFrame();
		ImGui.newFrame();
		processGui();
		ImGui.render();
		glImpl.renderDrawData(ImGui.getDrawData());
		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
	}

	private void processGui() {
		layers.forEach(layer -> {
			layer.create();
		});
	}

	@Override
	public void dispose() {
		glImpl.dispose();
		glfwImpl.dispose();
		ImGui.destroyContext();
	}
	
	public ImGuiImplGlfw getGlfwImpl() {
		return glfwImpl;
	}
	
	public void addGuiLayer(IGuiLayer layer) {
		layers.add(layer);
	}
	
	public void removeGuiLayer(IGuiLayer layer) {
		layers.remove(layer);
	}

}
