package app.gui;

import static imgui.ImGui.*;

import java.io.File;

import org.joml.Vector3f;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import app.renderers.MaterialRenderer;
import client.gui.IGuiLayer;
import client.rendering.materials.Material;
import client.rendering.materials.Texture2D;
import client.rendering.materials.TextureType;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.utils.registries.Registries;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;

public class Layers {
	
	public static final IGuiLayer modelSettings = new IGuiLayer() {
		ImInt modelIndex = new ImInt(0);
		ImInt meshIndex = new ImInt(0);
		
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
		Vector3f tempVec3 = new Vector3f();
		
		@Override
		public void create() {
			begin("Model settings", ImGuiWindowFlags.AlwaysUseWindowPadding | ImGuiWindowFlags.NoDocking);
				
				if (combo("Model", modelIndex, Registries.Models.getModelNameArray())) {
					currentmodel = Registries.Models.getStaticModel(Registries.Models.getModelNameArray()[modelIndex.get()]);
					meshIndex.set(0);
					currentmesh = currentmodel.getMeshes().get(0);
					posX.set(currentmodel.getTransform().getPosition().x());
					posY.set(currentmodel.getTransform().getPosition().y());
					posZ.set(currentmodel.getTransform().getPosition().z());
					rotX.set(currentmodel.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).x());
					rotY.set(currentmodel.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).y());
					rotZ.set(currentmodel.getTransform().getRotation().getEulerAnglesXYZ(tempVec3).z());
					scaleX.set(currentmodel.getTransform().getScale().x());
					scaleY.set(currentmodel.getTransform().getScale().y());
					scaleZ.set(currentmodel.getTransform().getScale().z());
					loadMaterial();
					MaterialRenderer.instance.setModel(currentmodel);
				}
				if (currentmodel != null) {
					if (combo("Mesh", meshIndex, currentmodel.getMeshNames())) {
						currentmesh = currentmodel.getMeshes().get(meshIndex.get());
						loadMaterial();
					}
				}
				if (currentmesh != null) {
					if (treeNode("Transform")) {
						if (treeNode("Position")) {
							if (inputFloat("X", posX)) {
								currentmodel.getTransform().getPosition().x = posX.get();
							}
							if (inputFloat("Y", posY)) {
								currentmodel.getTransform().getPosition().y = posY.get();
							}
							if (inputFloat("Z", posZ)) {
								currentmodel.getTransform().getPosition().z = posZ.get();
							}
							treePop();
						}
						if (treeNode("Rotation")) {
							if (inputFloat("X", rotX)) {
								currentmodel.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
							}                   
							if (inputFloat("Y", rotY)) {
								currentmodel.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
							}                   
							if (inputFloat("Z", rotZ)) {
								currentmodel.getTransform().setRotation(rotX.get(), rotY.get(), rotZ.get());
							}
							treePop();
						}
						if (treeNode("Scale")) {
							if (inputFloat("X", scaleX)) {
								currentmodel.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
							}                   
							if (inputFloat("Y", scaleY)) {
								currentmodel.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
							}                   
							if (inputFloat("Z", scaleZ)) {
								currentmodel.getTransform().setScale(scaleX.get(), scaleY.get(), scaleZ.get());
							}
							treePop();
						}
						treePop();
					}
				}
				if (currentmaterial != null) {
					if (treeNode("Material")) {
						if (colorEdit4("Base Colour", baseColour)) {
							currentmaterial.getDiffuseColour().set(baseColour);
						}
						textureInterface(TextureType.BASE_COLOUR, baseColourIndex);
						textureInterface(TextureType.NORMAL, normalIndex);
						textureInterface(TextureType.METALLIC, metallicIndex);
						textureInterface(TextureType.ROUGHNESS, roughnessIndex);
						treePop();
					}
				}
			end();
		}

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
