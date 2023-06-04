package client.rendering.utils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.rendering.materials.Material;
import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.utils.MathUtil;

public class ModelLoader {

	private static LinkedList<Float> vertices = new LinkedList<>();;
	private static LinkedList<Float> normals = new LinkedList<>();
	private static LinkedList<Float> texCoords = new LinkedList<>();
	private static LinkedList<Integer> indices = new LinkedList<>();
	private static LinkedList<Float> tangents = new LinkedList<>();
	private static LinkedList<Float> bitangents = new LinkedList<>();
//	private static List<Material> materials = new ArrayList<>();

	private static int flags =  Assimp.aiProcess_Triangulate | Assimp.aiProcess_CalcTangentSpace;

	public static Model loadModel(String name) {
		clearData();
//		materials.clear();
		String path = Files.getCommonFolderPath(CommonFolders.Models) + "/" + name;
		Log.debug(ModelLoader.class, "Starting model loading for: " + name);

		AIScene scene = Assimp.aiImportFile(path, flags);
		if (scene == null) {
			Log.error(ModelLoader.class, "Failed to load model: " + path);
			Log.error(ModelLoader.class, Assimp.aiGetErrorString());
		}

//		for (int i = 0; i < scene.mNumMaterials(); i++) {
//			AIMaterial material = AIMaterial.create(scene.mMaterials().get(i));
//			materials.add(processMaterial(material));
//			material.free();
//		}

		List<Mesh> meshes = new ArrayList<>();
		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
//			meshes.add(processMesh(mesh, materials));
			meshes.add(processMesh(mesh));
			mesh.free();
			clearData();
		}

		scene.free();

		Log.debug(ModelLoader.class, "Model loaded: " + name + " with a total of " + meshes.size() + " meshes.");
		Model model = new Model(name, meshes);
		return model;
	}

	private static void clearData() {
		vertices.clear();
		normals.clear();
		texCoords.clear();
		indices.clear();
		tangents.clear();
		bitangents.clear();
	}

//	private static Material processMaterial(AIMaterial material) {
//		Material mat = new Material();
//		AIColor4D colour = AIColor4D.create();
//		AIString path = AIString.create();
//		
//		//Load diffuse texture
//		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
//		if (path.length() > 0 && path != null) {
//			String[] str = path.dataString().split("\\\\");
//			String name = str[str.length-1];
//			mat.setTexture(TextureType.BASE_COLOUR, Registries.Textures.get2DTexture(name));
//		} 
//		Assimp.aiGetMaterialColor(material, Assimp.AI_MATKEY_COLOR_DIFFUSE, 0, 0, colour);
//		mat.setDiffuseColour(colour.r(), colour.g(), colour.b(), colour.a());
//		
//		//Loads normal texture
//		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_NORMALS, 0, path, (IntBuffer) null, null, null, null, null, null);
//		if (path.length() > 0 && path != null) {
//			String[] str = path.dataString().split("\\\\");
//			String name = str[str.length-1];
//			mat.setTexture(TextureType.NORMAL, Registries.Textures.get2DTexture(name));
//		} 
//		
//		//Load displacement texture
//		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_DISPLACEMENT, 0, path, (IntBuffer) null, null, null, null, null, null);
//		if (path.length() > 0 && path != null) {
//			String[] str = path.dataString().split("\\\\");
//			String name = str[str.length-1];
//			mat.setTexture(TextureType.DISPLACEMENT, Registries.Textures.get2DTexture(name));
//		} 
//		
//		//Load displacement texture
//		Assimp.aiGetMaterialTexture(material, Assimp.aiTextureType_SPECULAR, 0, path, (IntBuffer) null, null, null, null, null, null);
//		if (path.length() > 0 && path != null) {
//			String[] str = path.dataString().split("\\\\");
//			String name = str[str.length-1];
//			mat.setTexture(TextureType.ROUGHNESS, Registries.Textures.get2DTexture(name));
//		} 
//		
//		//path.free();
//		//colour.free();
//		
//		while (GL30.glGetError() != GL30.GL_NO_ERROR) {
//			Log.error(ModelLoader.class, "Opengl Error: " + GL30.glGetError());
//		}
//		
//		return mat;
//	}

	private static Mesh processMesh(AIMesh mesh/*, List<Material> mats*/) {
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D vertex = mesh.mVertices().get(i);
			vertices.add(vertex.x());
			vertices.add(vertex.y());
			vertices.add(vertex.z());
			
			AIVector3D normal = mesh.mNormals().get(i);
			normals.add(normal.x());
			normals.add(normal.y());
			normals.add(normal.z());
			//vertex.free();
			//normal.free();
			
			AIVector3D tangent = mesh.mTangents().get(i);
			tangents.add(tangent.x());
			tangents.add(tangent.y());
			tangents.add(tangent.z());
			
			AIVector3D bitangent = mesh.mTangents().get(i);
			bitangents.add(bitangent.x());
			bitangents.add(bitangent.y());
			bitangents.add(bitangent.z());
			
			AIVector3D tc = mesh.mTextureCoords(0).get(i);
			texCoords.add(tc.x());
			texCoords.add(tc.y());
		}

//		AIVector3D.Buffer aiT = mesh.mTextureCoords(0);
//		while (aiT.hasRemaining()) {
//			AIVector3D v = aiT.get();
//			texCoords.add(v.x());
//			texCoords.add(v.y());
//			//v.free();
//		}
		//aiT.free();
		
		
		AIFace.Buffer faces = mesh.mFaces();
		while (faces.hasRemaining()) {
			IntBuffer b = faces.get().mIndices();
			while (b.hasRemaining()) {
				indices.add(b.get());
			}
		}
		faces.free();

		int vaoId = Loader.loadModelData(MathUtil.listToArrayFloat(vertices), MathUtil.listToArrayFloat(texCoords), MathUtil.listToArrayFloat(normals), MathUtil.ListToArrayInteger(indices), MathUtil.listToArrayFloat(tangents), MathUtil.listToArrayFloat(bitangents));
		
		return new Mesh(vaoId, Material.DEFAULT, indices.size(), mesh.mName().dataString());
	}

}
