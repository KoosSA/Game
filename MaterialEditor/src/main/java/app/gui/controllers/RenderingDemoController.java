package app.gui.controllers;

import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.koossa.logger.Log;

import app.renderers.MaterialRenderer;
import client.io.input.InputStates;
import client.io.input.receivers.GameInputReceiver;
import client.io.input.receivers.handlers.IGeneralInputHandler;
import client.io.input.receivers.handlers.IKeyInputHandler;
import client.logic.internalEvents.IUpdatable;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.rendering.utils.Transform;
import client.utils.Globals;
import client.utils.registries.Registries;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class RenderingDemoController implements ScreenController, IUpdatable, IGeneralInputHandler {

	DropDown<String> normalDD;
	DropDown<String> diffuseDD;
	DropDown<String> speccularDD;
	DropDown<String> metallicDD;
	DropDown<String> modelDD;
	DropDown<String> meshDD;
	
	TextField posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ;
	
	Label inputState;
	
	Model currentModel;
	Mesh currentMesh;
	Material currentMaterial;
	
	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {
		normalDD = screen.findNiftyControl("normalPicker", DropDown.class);
		diffuseDD = screen.findNiftyControl("diffusePicker", DropDown.class);
		speccularDD = screen.findNiftyControl("specularPicker", DropDown.class);
		metallicDD = screen.findNiftyControl("metallicPicker", DropDown.class);
		modelDD = screen.findNiftyControl("modelPicker", DropDown.class);
		meshDD = screen.findNiftyControl("meshPicker", DropDown.class);
		posX = screen.findNiftyControl("posX", TextField.class);
		posY = screen.findNiftyControl("posY", TextField.class);
		posZ = screen.findNiftyControl("posZ", TextField.class);
		rotX = screen.findNiftyControl("rotX", TextField.class);
		rotY = screen.findNiftyControl("rotY", TextField.class);
		rotZ = screen.findNiftyControl("rotZ", TextField.class);
		scaleX = screen.findNiftyControl("scaleX", TextField.class);
		scaleY = screen.findNiftyControl("scaleY", TextField.class);
		scaleZ = screen.findNiftyControl("scaleZ", TextField.class);
		inputState = screen.findNiftyControl("inputState", Label.class);
		inputState.setText(Globals.input.getCurrentInputState().name());
	}

	@Override
	public void onStartScreen() {
		registerUpdatable();
		registerInputHandler(InputStates.GAME);
	}

	@Override
	public void onEndScreen() {
		unRegisterUpdatable();
		unregisterInputHandler(InputStates.GAME);
	}

	public void refreshTextures() {
		Log.debug(this, "Refreshing texture list.");
		normalDD.clear();
		normalDD.addItem("NONE");
		normalDD.addAllItems(Registries.Textures.getTexture2DNameList());
		diffuseDD.clear();
		diffuseDD.addItem("NONE");
		diffuseDD.addAllItems(Registries.Textures.getTexture2DNameList());
		speccularDD.clear();
		speccularDD.addItem("NONE");
		speccularDD.addAllItems(Registries.Textures.getTexture2DNameList());
		metallicDD.clear();
		metallicDD.addItem("NONE");
		metallicDD.addAllItems(Registries.Textures.getTexture2DNameList());
		if (currentMesh != null) {
			getTexFromMesh();
		} else {
			normalDD.selectItemByIndex(0);
			speccularDD.selectItemByIndex(0);
			diffuseDD.selectItemByIndex(0);
		}
	}
	
	private void getTexFromMesh() {
		if (currentMesh == null) return;
		currentMaterial = currentMesh.getMaterial();
		try {
			normalDD.selectItem(currentMaterial.getTexture(TextureType.NORMAL).getName());
		}catch (Exception e) {};
		try {
			diffuseDD.selectItem(currentMaterial.getTexture(TextureType.BASE_COLOUR).getName());
		}catch (Exception e) {};
		try {
			speccularDD.selectItem(currentMaterial.getTexture(TextureType.ROUGHNESS).getName());
		}catch (Exception e) {};
		try {
			metallicDD.selectItem(currentMaterial.getTexture(TextureType.METALLIC).getName());
		}catch (Exception e) {};
	}

	public void applyMaterial() {
		currentMaterial.setTexture(TextureType.BASE_COLOUR, Registries.Textures.get2DTexture(diffuseDD.getSelection()));
		currentMaterial.setTexture(TextureType.NORMAL, Registries.Textures.get2DTexture(normalDD.getSelection()));
		currentMaterial.setTexture(TextureType.ROUGHNESS, Registries.Textures.get2DTexture(speccularDD.getSelection()));
		currentMaterial.setTexture(TextureType.METALLIC, Registries.Textures.get2DTexture(metallicDD.getSelection()));
	}
	
	public void refreshModels() {
		Log.debug(this, "Refreshing model list.");
		modelDD.clear();
		modelDD.addItem("NONE");
		modelDD.addAllItems(Registries.Models.getModelNameList());
		refreshTextures();
	}
	
	public void applyModel() {
		currentModel = Registries.Models.getStaticModel(modelDD.getSelection());
		MaterialRenderer.instance.setModel(currentModel);
		if (currentModel == null) return;
		meshDD.clear();
//		meshDD.addAllItems(currentModel.getMeshes());
		currentModel.getMeshes().forEach(mesh -> {
			meshDD.addItem(mesh.getName());
		});
		meshDD.selectItemByIndex(0);
		Transform t = currentModel.getTransform();
		Vector3f r = new Vector3f();
		t.getRotation().getEulerAnglesXYZ(r);
		posX.setText(String.valueOf(t.getPosition().x()));
		posY.setText(String.valueOf(t.getPosition().y()));
		posZ.setText(String.valueOf(t.getPosition().z()));
		rotX.setText(String.valueOf(Math.toDegrees(r.x())));
		rotY.setText(String.valueOf(Math.toDegrees(r.y())));
		rotZ.setText(String.valueOf(Math.toDegrees(r.z())));
		scaleX.setText(String.valueOf(t.getScale().x()));
		scaleY.setText(String.valueOf(t.getScale().y()));
		scaleZ.setText(String.valueOf(t.getScale().z()));
		applyMesh();
	}
	
	public void applyMesh() {
		if (currentModel == null) return;
		currentMesh = currentModel.getMeshes().get(meshDD.getSelectedIndex());
		getTexFromMesh();
	}

	@Override
	public void update(float delta) {
		updateTransform();
	}

	private void updateTransform() {
		if (currentModel == null) return;
		try {
			currentModel.getTransform().setPosition(Float.parseFloat(posX.getRealText()), Float.parseFloat(posY.getRealText()), Float.parseFloat(posZ.getRealText()));
			currentModel.getTransform().setRotation(Float.parseFloat(rotX.getRealText()), Float.parseFloat(rotY.getRealText()), Float.parseFloat(rotZ.getRealText()));
			currentModel.getTransform().setScale(Float.parseFloat(scaleX.getRealText()), Float.parseFloat(scaleY.getRealText()), Float.parseFloat(scaleZ.getRealText()));
		} catch (Exception e) {};
	}

	@Override
	public void handleInputs(GameInputReceiver input, float delta) {
		if (input.isKeyJustPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
			Globals.input.setInputReceiver(InputStates.GUI);
			inputState.setText(Globals.input.getCurrentInputState().name());
		}
	}
	
	public void switchToGameState() {
		Globals.input.setInputReceiver(InputStates.GAME);
		inputState.setText(Globals.input.getCurrentInputState().name());
	}

}
