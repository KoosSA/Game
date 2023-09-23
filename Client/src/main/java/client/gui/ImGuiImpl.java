package client.gui;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import client.logic.internalEvents.IDisposable;
import client.utils.Globals;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiImpl implements IDisposable{
	
	private ImGuiImplGlfw glfwImpl;
	private ImGuiImplGl3 glImpl;
	private ImGuiIO io;
	private Map<String, ImGuiLayer> alllayers;
	private Map<String, ImGuiLayer> visibleLayers;
	
	public ImGuiImpl() {
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
		alllayers = new HashMap<String, ImGuiLayer>();
		visibleLayers = new HashMap<String, ImGuiLayer>();
		
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
		visibleLayers.forEach((id, layer) -> {
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
	
	public ImGuiLayer addGuiLayer(String guiId, ImGuiLayer layer) {
		alllayers.putIfAbsent(guiId, layer);
		return layer;
	}
	
	public void removeGuiLayer(String guiId) {
		if (alllayers.containsKey(guiId)) {
			hide(guiId);
			alllayers.remove(guiId);
		}
	}
	
	public void show(String guiId) {
		ImGuiLayer layer = alllayers.getOrDefault(guiId, null);
		if (layer == null) return;
		visibleLayers.putIfAbsent(guiId, layer);
	}
	
	public void hide(String guiId) {
		if (visibleLayers.containsKey(guiId)) {
			visibleLayers.remove(guiId);
		}
	}

}
