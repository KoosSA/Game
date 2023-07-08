package client.rendering.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import client.rendering.cameras.Camera;
import client.rendering.shaders.TerrainShader;
import client.utils.Globals;
import client.utils.registries.Registries;

public class TerrainRenderer extends BaseRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(Camera cam) {
		super(cam);
		shader = new TerrainShader();
		loadStaticCamData(cam);
	}

	private void loadStaticCamData(Camera cam) {
		Log.debug(this, "Updating camera data in shader.");
		shader.start();
		shader.loadProjectionMatrix(cam);
		shader.stop();
	}

	@Override
	protected void render() {
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		GL30.glEnable(GL30.GL_CULL_FACE);
		GL30.glCullFace(GL30.GL_BACK);
		
		shader.start();
		shader.loadViewMatrix(cam);
		GL30.glBindVertexArray(Globals.terrain.getVaoId());
		
		shader.loadAmbientLight(Registries.Lights.getAmbientLight());
		shader.loadDirectionalLight(Registries.Lights.getDirectionalLight());
		shader.loadCameraPosition(cam);
		
		Globals.terrain.getChunksToRender().forEach(chunk -> {
				shader.loadTransformationMatrix(chunk.getTransform());
				
				GL30.glDrawElements(GL11.GL_TRIANGLES, chunk.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
					
				GL30.glBindVertexArray(0);
				
		});
		
		shader.stop();
		
		int err = GL30.GL_NO_ERROR;
		while ((err = GL30.glGetError()) != GL30.GL_NO_ERROR) {
			Log.error(this, "OpenGL error: " + err);
		}
	}

	@Override
	public void dispose() {
		shader.dispose();
	}

	@Override
	public void onResize(int width, int height) {
		loadStaticCamData(cam);
	}
	
	

}
