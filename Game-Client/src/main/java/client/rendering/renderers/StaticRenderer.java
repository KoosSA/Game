package client.rendering.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import client.rendering.cameras.Camera;
import client.rendering.objects.Model;
import client.rendering.shaders.StaticShader;
import client.utils.registries.Registries;

public class StaticRenderer extends BaseRenderer {
	
	private StaticShader shader;
	
	public StaticRenderer(Camera cam) {
		super(cam);
		shader = new StaticShader();
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
		
		shader.loadAmbientLight(Registries.Lights.getAmbientLight());
		shader.loadDirectionalLight(Registries.Lights.getDirectionalLight());
		shader.loadCameraPosition(cam);
		
		Model model = Registries.Models.getStaticModel("t.fbx");
		//Model model = Registries.Models.getStaticModel("uc_uv_sphere.fbx");
		shader.loadTransformationMatrix(model.getTransform());
		model.getMeshes().forEach(mesh -> {
				shader.loadMaterial(mesh.getMaterial());
			
				GL30.glBindVertexArray(mesh.getVaoId());
				
				GL30.glDrawElements(GL11.GL_TRIANGLES, mesh.getNumberOfIndices(), GL11.GL_UNSIGNED_INT, 0);
				
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
