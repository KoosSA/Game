package client.rendering.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import client.rendering.objects.Mesh;
import client.rendering.objects.Model;
import client.utils.MathUtil;

public class ModelLoader {

	private LinkedList<Float> vertices;
	private LinkedList<Float> normals;
	private LinkedList<Float> texCoords;
	private LinkedList<Integer> indices;
	private List<Mesh> meshes;

	private int flags = Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
			| Assimp.aiProcess_LimitBoneWeights | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_GenUVCoords
			| Assimp.aiProcess_FixInfacingNormals | Assimp.aiProcess_GenBoundingBoxes;

	public ModelLoader() {
		vertices = new LinkedList<>();
		normals = new LinkedList<>();
		texCoords = new LinkedList<>();
		indices = new LinkedList<>();
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
		
		int numMats = scene.mNumMaterials();
		int numMesh = scene.mNumMeshes();

		for (int i = 0; i<numMats; i++) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get(i));
			processMaterial(material);
			material.free();
		}

		meshes = new ArrayList<>();
		for (int i = 0; i<numMesh; i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
			meshes.add(processMesh(mesh));
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
	}

	private void processMaterial(AIMaterial material) {

	}

	private Mesh processMesh(AIMesh mesh) {
		for (int i=0; i<mesh.mNumVertices(); i++) {
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
		
		for (int i=0; i<mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int u=0; u<face.mNumIndices(); u++) {
				indices.add(face.mIndices().get(u));
			}
			face.free();
		}
		return new Mesh(MathUtil.listToArrayFloat(vertices), MathUtil.listToArrayFloat(normals), MathUtil.listToArrayFloat(texCoords), MathUtil.ListToArrayInteger(indices));
	}

}