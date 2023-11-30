package app.gui;

import static imgui.ImGui.begin;
import static imgui.ImGui.button;
import static imgui.ImGui.checkbox;
import static imgui.ImGui.colorEdit4;
import static imgui.ImGui.combo;
import static imgui.ImGui.end;
import static imgui.ImGui.inputFloat;
import static imgui.ImGui.text;
import static imgui.ImGui.treeNode;
import static imgui.ImGui.treePop;

import java.io.File;

import org.joml.Vector3f;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.gui.ImGuiLayer;
import client.rendering.materials.Material;
import client.rendering.materials.Texture2D;
import client.rendering.materials.TextureType;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.rendering.objects.ModelInstance;
import client.rendering.utils.ModelManager;
import client.utils.registries.Registries;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;

public class Layers {
	
	public static final ImGuiLayer modelSettings = new ImGuiLayer() {
		ImInt modelIndex = new ImInt(0);
		ImInt meshIndex = new ImInt(0);
		ImInt instanceIndex = new ImInt(0);
		
		ImInt baseColourIndex = new ImInt(0);
		ImInt metallicIndex = new ImInt(0);
		ImInt roughnessIndex = new ImInt(0);
		ImInt normalIndex = new ImInt(0);
		
		ImFloat posX = new ImFloat();
		ImFloat posY = new ImFloat();
		ImFloat posZ = new ImFloat();
		ImFloat rotX = new ImFloat();
		ImFloat rotY = new ImFloat();
		ImFloat rotZ = new ImFloat();
		ImFloat scaleX = new ImFloat();
		ImFloat scaleY = new ImFloat();
		ImFloat scaleZ = new ImFloat();
		
		
		float[] baseColour = new float[4];
		
		Model currentmodel;
		Mesh currentmesh;
		Material currentmaterial;
		ModelInstance currentModelInstance;
		Vector3f tempVec3 = new Vector3f();
		
		@Override
		public void create() {
			begin("Model settings", ImGuiWindowFlags.AlwaysUseWindowPadding | ImGuiWindowFlags.NoDocking);
				importModel();
				if (Registries.Models.getModelNameList().size() <= 0) {
					text("Mo models loaded as of yet.");
					end();
					return;
				}
				selectModel();
				materialSettings();
				if (treeNode("Instance")) {
					addInstance();
					selectInstance();
					if (currentModelInstance != null) {
						transformSettings();
					}
					treePop();
				}
				
			end();
		}

		private void addInstance() {
			if (button("Add model instance")) {
				ModelInstance i = ModelManager.addModelInstanceToWorld(new ModelInstance(currentmodel));
				if (currentModelInstance == null) currentModelInstance = i;
			}
		}

		private void selectModel() {
			if (combo("Model", modelIndex, Registries.Models.getModelNameArray())) {
				currentmodel = Registries.Models.getStaticModel(Registries.Models.getModelNameArray()[modelIndex.get()]);
				meshIndex.set(0);
				currentmesh = currentmodel.getMeshes().get(0);
				loadMaterial();
				currentModelInstance = null;
			}
		}

		private void selectMesh() {
			if (currentmodel != null) {
				if (combo("Mesh", meshIndex, currentmodel.getMeshNames())) {
					currentmesh = currentmodel.getMeshes().get(meshIndex.get());
					loadMaterial();
				}
			}
		}

		private void selectInstance() {
			if (ModelManager.getInstances().getOrDefault(currentmodel, null) == null) {
				currentModelInstance = null;
				return;
			}
			String[] inst = new String[ModelManager.getInstances().get(currentmodel).size()];
			for (int i = 0; i < inst.length; i++) {
				inst[i] = String.valueOf(i);
			}
			if (combo("Instance", instanceIndex, inst)) {
				currentModelInstance = ModelManager.getInstances().get(currentmodel).get(instanceIndex.get());
			}
			getTransformFromInstance();
		}
		
		private void getTransformFromInstance() {
			if (currentModelInstance == null) return;
			posX.set(currentModelInstance.getTransform().getPosition().x());
			posY.set(currentModelInstance.getTransform().getPosition().y());
			posZ.set(currentModelInstance.getTransform().getPosition().z());
			rotX.set(currentModelInstance.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).x());
			rotY.set(currentModelInstance.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).y());
			rotZ.set(currentModelInstance.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).z());
			scaleX.set(currentModelInstance.getTransform().getScale().x());
			scaleY.set(currentModelInstance.getTransform().getScale().y());
			scaleZ.set(currentModelInstance.getTransform().getScale().z());
		}
		
		private void materialSettings() {
			if (currentmaterial != null) {
				if (treeNode("Material")) {
					selectMesh();
					if (currentmesh == null) {
						treePop();
						return;
					}
					if (colorEdit4("Base Colour", baseColour)) {
						currentmaterial.getDiffuseColour().set(baseColour);
					}
					textureInterface(TextureType.BASE_COLOUR, baseColourIndex);
					textureInterface(TextureType.NORMAL, normalIndex);
					textureInterface(TextureType.METALLIC, metallicIndex);
					textureInterface(TextureType.ROUGHNESS, roughnessIndex);
					if (button("Save Material")) {
						
						currentmaterial.save(true, currentmodel.getName() + "_" + currentmesh.getName() + ".json", "Materials");
						
					}
					treePop();
				}
			}
		}

		private void transformSettings() {
			if (treeNode("Transform")) {
				if (treeNode("Position")) {
					if (inputFloat("X", posX)) {
						currentModelInstance.getTransform().getPosition().x = posX.get();
					}
					if (inputFloat("Y", posY)) {
						currentModelInstance.getTransform().getPosition().y = posY.get();
					}
					if (inputFloat("Z", posZ)) {
						currentModelInstance.getTransform().getPosition().z = posZ.get();
					}
					treePop();
				}
				if (treeNode("Rotation")) {
					if (inputFloat("X", rotX)) {
						currentModelInstance.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
					}                   
					if (inputFloat("Y", rotY)) {
						currentModelInstance.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
					}                   
					if (inputFloat("Z", rotZ)) {
						currentModelInstance.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
					}
					treePop();
				}
				if (treeNode("Scale")) {
					if (inputFloat("X", scaleX)) {
						currentModelInstance.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
					}                   
					if (inputFloat("Y", scaleY)) {
						currentModelInstance.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
					}                   
					if (inputFloat("Z", scaleZ)) {
						currentModelInstance.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
					}
					treePop();
				}
				treePop();
			}
		}

		private void importModel() {
			if (button("Import New Model")) {
				ImGuiFileDialog.openDialog("Import Models", "Import Model", ".fbx,.obj", Files.getCommonFolderPath(CommonFolders.Models), "", 1, 1, ImGuiFileDialogFlags.None);
			}
			if (ImGuiFileDialog.display("Import Models", ImGuiWindowFlags.NoCollapse, 600, 400, 800, 600)) {
				if (ImGuiFileDialog.isOk()) {
					String filename = ImGuiFileDialog.getCurrentFileName();
					String path = ImGuiFileDialog.getCurrentPath();
					File newFile = new File(path, filename);
					File targetFile = new File(Files.getCommonFolder(CommonFolders.Models), filename);
					if (!newFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
						Log.debug(this, "Importing model to models folder: " + filename);
						newFile.renameTo(targetFile);
					}
					Model m = Registries.Models.getStaticModel(filename);
					if (m == null) return;
					if (currentmodel == null) currentmodel = m;
					meshIndex.set(0);
					currentmesh = currentmodel.getMeshes().get(0);
					loadMaterial();
				}
				ImGuiFileDialog.close();
			}
		}
		
		/*private void importTexture(TextureType texType) {
			if (button("Import New Texture")) {
				ImGuiFileDialog.openDialog("Import Texture", "Import Texture", ".png,.jpg", Files.getCommonFolderPath(CommonFolders.Textures), "", 1, 1, ImGuiFileDialogFlags.None);
			}
			if (ImGuiFileDialog.display("Import Texture", ImGuiWindowFlags.NoCollapse, 600, 400, 800, 600)) {
				if (ImGuiFileDialog.isOk()) {
					String filename = ImGuiFileDialog.getCurrentFileName();
					String path = ImGuiFileDialog.getCurrentPath();
					File newFile = new File(path, filename);
					File targetFile = new File(Files.getCommonFolder(CommonFolders.Textures), filename);
					if (!newFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
						Log.debug(this, "Importing texture to textures folder: " + filename);
						newFile.renameTo(targetFile);
					}
					Texture2D t = Registries.Textures.get2DTexture(filename, texType);
					//Model m = Registries.Models.getStaticModel(filename);
					if (t == null) return;
					//if (currentmodel == null) currentmodel = m;
					//meshIndex.set(0);
					//currentmesh = currentmodel.getMeshes().get(0);
					//loadMaterial();
				}
				ImGuiFileDialog.close();
			}
		}*/

		private void textureInterface(TextureType texType, ImInt index) {
			if (checkbox("Use " + texType.name() + " Map", currentmaterial.isUseTexture(texType))) {
				 if (currentmaterial.isUseTexture(texType)) {
					currentmaterial.removeTexture(texType);
				} else {
					currentmaterial.setTexture(texType, Registries.Textures.get2DTexture(index.get(), texType));
					if (currentmaterial.getTexture(texType) == null) {
						ImGuiFileDialog.openDialog(texType.name() + "import", "Import " + texType.name() + " texture", ".png,.jpg", Files.getCommonFolderPath(CommonFolders.Textures), "", 1, 1, ImGuiFileDialogFlags.None);
					}
				}
			}
			if (currentmaterial.isUseTexture(texType)) {
				if (combo(texType.name() + " Map", index, Registries.Textures.getTexture2DNameArray(texType))) {
					currentmaterial.setTexture(texType, Registries.Textures.get2DTexture(index.get(), texType));
				}
				if (button("Import new " + texType.name() + " texture")) {
					ImGuiFileDialog.openDialog(texType.name() + "import", "Import " + texType.name() + " texture", ".png,.jpg", Files.getCommonFolderPath(CommonFolders.Textures), "", 1, 1, ImGuiFileDialogFlags.None);
				}
			}
			if (ImGuiFileDialog.display(texType.name() + "import", ImGuiWindowFlags.NoCollapse, 600, 400, 800, 600)) {
				if (ImGuiFileDialog.isOk()) {
					String filename = ImGuiFileDialog.getCurrentFileName();
					String path = ImGuiFileDialog.getCurrentPath();
					File newFile = new File(path, filename);
					File targetFile = new File(Files.getCommonFolder(CommonFolders.Textures), filename);
					if (!newFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
						Log.debug(this, "Importing texture to texture folder: " + filename);
						newFile.renameTo(targetFile);
					}
					Texture2D tex = Registries.Textures.get2DTexture(filename, texType);
					if (tex != null) {
						currentmaterial.setTexture(texType, tex);
					}
				}
				ImGuiFileDialog.close();
			}
		}

		private void loadMaterial() {
			if (currentmesh == null) {
				currentmaterial = null;
				return;
			}
			currentmaterial = currentmesh.getMaterial();
			baseColour[0] = currentmaterial.getDiffuseColour().x();
			baseColour[1] = currentmaterial.getDiffuseColour().y();
			baseColour[2] = currentmaterial.getDiffuseColour().z();
			baseColour[3] = currentmaterial.getDiffuseColour().w();
			loadMaterialTexture(TextureType.BASE_COLOUR, baseColourIndex);
			loadMaterialTexture(TextureType.NORMAL, normalIndex);
			loadMaterialTexture(TextureType.METALLIC, metallicIndex);
			loadMaterialTexture(TextureType.ROUGHNESS, roughnessIndex);
		}
		
		private void loadMaterialTexture(TextureType texType, ImInt index) {
			if (currentmaterial.isUseTexture(texType)) {
				index.set(Registries.Textures.getTextureIndex(currentmaterial.getTexture(texType).getName(), texType));
			}
		}
	};

}
