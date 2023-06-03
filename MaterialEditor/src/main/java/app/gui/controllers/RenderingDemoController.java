package app.gui.controllers;

import org.joml.Math;
import org.joml.Vector3f;

import com.koossa.logger.Log;

import app.renderers.MaterialRenderer;
import client.logic.internalEvents.IUpdatable;
import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.rendering.utils.Transform;
import client.utils.registries.Registries;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class RenderingDemoController implements ScreenController, IUpdatable {

	DropDown<String> normalDD;
	DropDown<String> diffuseDD;
	DropDown<String> speccularDD;
	DropDown<String> modelDD;
	DropDown<Mesh> meshDD;
	
	TextField posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ;
	
	Model currentModel;
	Mesh currentMesh;
	Material currentMaterial;
	
	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {
		normalDD = screen.findNiftyControl("normalPicker", DropDown.class);
		diffuseDD = screen.findNiftyControl("diffusePicker", DropDown.class);
		speccularDD = screen.findNiftyControl("specularPicker", DropDown.class);
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
	}

	@Override
	public void onStartScreen() {
		registerUpdatable();
	}

	@Override
	public void onEndScreen() {
		unRegisterUpdatable();
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
			diffuseDD.selectItem(currentMaterial.getTexture(TextureType.DIFFUSE).getName());
		}catch (Exception e) {};
		try {
			speccularDD.selectItem(currentMaterial.getTexture(TextureType.SPECULAR).getName());
		}catch (Exception e) {};
	}

	public void applyMaterial() {
		currentMaterial.setTexture(TextureType.DIFFUSE, Registries.Textures.get2DTexture(diffuseDD.getSelection()));
		currentMaterial.setTexture(TextureType.NORMAL, Registries.Textures.get2DTexture(normalDD.getSelection()));
		currentMaterial.setTexture(TextureType.SPECULAR, Registries.Textures.get2DTexture(speccularDD.getSelection()));
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
		if (currentModel == null) return;
		meshDD.clear();
		meshDD.addAllItems(currentModel.getMeshes());
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
		MaterialRenderer.instance.model = currentModel;
	}
	
	public void applyMesh() {
		if (currentModel == null) return;
		currentMesh = meshDD.getSelection();
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

}
