package client.rendering.objects;

import java.util.LinkedList;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector2D;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class ModelLoader {

	private LinkedList<Float> vertices;
	private LinkedList<Float> normals;
	private LinkedList<Float> texCoords;
	private LinkedList<Integer> indices;

	private int flags = Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices
			| Assimp.aiProcess_LimitBoneWeights | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_GenUVCoords
			| Assimp.aiProcess_FixInfacingNormals | Assimp.aiProcess_GenBoundingBoxes;

	public ModelLoader() {
		vertices = new LinkedList<>();
		normals = new LinkedList<>();
		texCoords = new LinkedList<>();
		indices = new LinkedList<>();
	}

	public void loadModel(String name) {
		clearData();
		String path = Files.getCommonFolderPath(CommonFolders.Models) + "/" + name;
		Log.debug(this, "Starting model loading for: " + path);

		AIScene scene = Assimp.aiImportFile(path, flags);

		while (scene.mMaterials().hasRemaining()) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get());
			processMaterial(material);
			material.free();
		}

		while (scene.mMeshes().hasRemaining()) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get());
			processMesh(mesh);
			mesh.free();
		}

		scene.free();
	}

	private void clearData() {
		vertices.clear();
		normals.clear();
		texCoords.clear();
		indices.clear();
	}

	private void processMaterial(AIMaterial material) {

	}

	private void processMesh(AIMesh mesh) {
		while (mesh.mVertices().hasRemaining()) {
			AIVector3D vertex = mesh.mVertices().get();
			vertices.add(vertex.x());
			vertices.add(vertex.y());
			vertices.add(vertex.z());
			vertex.free();
		}
		while (mesh.mNormals().hasRemaining()) {
			AIVector3D normal = mesh.mNormals().get();
			normals.add(normal.x());
			normals.add(normal.y());
			normals.add(normal.z());
			normal.free();
		}
		while (mesh.mTextureCoords().hasRemaining()) {
			AIVector2D texC = AIVector2D.create(mesh.mTextureCoords().get());
			texCoords.add(texC.x());
			texCoords.add(texC.y());
			texC.free();
		}
		while (mesh.mFaces().hasRemaining()) {
			AIFace face = mesh.mFaces().get();
			while (face.mIndices().hasRemaining()) {
				indices.add(face.mIndices().get());
			}
			face.free();
		}
	}

}
