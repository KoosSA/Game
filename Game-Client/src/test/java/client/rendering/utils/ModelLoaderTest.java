package client.rendering.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;

class ModelLoaderTest {
	
	float[] planeTestVerts = {-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f};

	@Test
	void testLoadModelPlaneVers() {
		ModelLoader l = new ModelLoader();
		Files.init("Resources", RootFileLocation.LOCAL);
		assertArrayEquals(planeTestVerts, l.loadModel("plane.fbx").getMeshes().get(0).getVertices());
	}

}
