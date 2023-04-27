package client.rendering.utils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.rendering.materials.Material;
import client.rendering.materials.TextureType;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.utils.MathUtil;
import client.utils.Registries;

public class ModelLoader {

	private LinkedList<Float> vertices;
	private LinkedList<Float> normals;
	private LinkedList<Float> texCoords;
	private LinkedList<Integer> indices;
	private List<Mesh> meshes;
	private List<Material> materials;

	private int flags = Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
			| Assimp.aiProcess_LimitBoneWeights | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_GenUVCoords
			| Assimp.aiProcess_FixInfacingNormals | Assimp.aiProcess_GenBoundingBoxes;

	public ModelLoader() {
		vertices = new LinkedList<>();
		normals = new LinkedList<>();
		texCoords = new LinkedList<>();
		indices = new LinkedList<>();
		materials = new LinkedList<>();
	}

	public Model loadModel(String name) {
		clearData();
		String path = Files.getCommonFolderPath(CommonFolders.Models) + "/" + name;
		Log.debug(this, "Starting model loading for: " + path);

		AIScene scene = Assimp.aiImportFile(path, flags);
		if (scene == null) {
			Log.error(this, "Failed to load model: " + path);
			Log.error(this, Assimp.aiGetErrorString());
		}

		for (int i = 0; i < scene.mNumMaterials(); i++) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get(i));
			materials.add(processMaterial(material));
			material.free();
		}

		meshes = new ArrayList<>(scene.mNumMeshes());
		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
			meshes.add(processMesh(mesh, materials));
			mesh.free();
		}

		scene.free();

		return new Model(meshes);
	}

	private void clearData() {
		vertices.clear();
		normals.clear();
		texCoords.clear();
		indices.clear();
		materials.clear();
	}

	private Material processMaterial(AIMaterial material) {
		Material mat = new Material();
		AIColor4D colour = AIColor4D.create();
		AIString path = AIString.create();
		
		//Load diffuse texture
		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		if (path.length() > 0 && path != null) {
			String[] str = path.dataString().split("\\\\");
			String name = str[str.length-1];
			mat.setTexture(TextureType.DIFFUSE, Registries.Textures.get2DTexture(name));
		} 
		Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_DIFFUSE, 0, 0, colour);
		mat.setDiffuseColour(colour.r(), colour.g(), colour.b(), colour.a());
		
		//Loads normal texture
		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_NORMALS, 0, path, (IntBuffer) null, null, null, null, null, null);
		if (path.length() > 0 && path != null) {
			String[] str = path.dataString().split("\\\\");
			String name = str[str.length-1];
			mat.setTexture(TextureType.NORMAL, Registries.Textures.get2DTexture(name));
		} 
		
		//Load displacement texture
		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DISPLACEMENT, 0, path, (IntBuffer) null, null, null, null, null, null);
		if (path.length() > 0 && path != null) {
			String[] str = path.dataString().split("\\\\");
			String name = str[str.length-1];
			mat.setTexture(TextureType.DISPLACEMENT, Registries.Textures.get2DTexture(name));
		} 
		
		//Load displacement texture
		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_SPECULAR, 0, path, (IntBuffer) null, null, null, null, null, null);
		if (path.length() > 0 && path != null) {
			String[] str = path.dataString().split("\\\\");
			String name = str[str.length-1];
			mat.setTexture(TextureType.SPECULAR, Registries.Textures.get2DTexture(name));
		} 
		
		path.free();
		colour.free();
		return mat;
	}

	private Mesh processMesh(AIMesh mesh, List<Material> mats) {
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D vertex = mesh.mVertices().get(i);
			vertices.add(vertex.x());
			vertices.add(vertex.y());
			vertices.add(vertex.z());
			AIVector3D normal = mesh.mNormals().get(i);
			normals.add(normal.x());
			normals.add(normal.y());
			normals.add(normal.z());
			vertex.free();
			normal.free();
		}

		AIVector3D.Buffer aiT = mesh.mTextureCoords(0);
		while (aiT.hasRemaining()) {
			AIVector3D v = aiT.get();
			texCoords.add(v.x());
			texCoords.add(v.y());
			v.free();
		}
		aiT.free();

		for (int i = 0; i < mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int u = 0; u < face.mNumIndices(); u++) {
				indices.add(face.mIndices().get(u));
			}
			face.free();
		}

		return new Mesh(Loader.loadModelData(MathUtil.listToArrayFloat(vertices), MathUtil.listToArrayFloat(texCoords), MathUtil.listToArrayFloat(normals), MathUtil.ListToArrayInteger(indices)), mats.get(mesh.mMaterialIndex()), indices.size());
	}

}
