package app.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.koossa.logger.Log;

import client.rendering.cameras.Camera;
import client.rendering.objects.Model;
import client.rendering.renderers.BaseRenderer;
import client.rendering.shaders.StaticShader;
import client.utils.registries.Registries;

public class MaterialRenderer extends BaseRenderer {
	
	public Model model = null;
	public static MaterialRenderer instance;
	
	public MaterialRenderer(Camera cam) {
		super(cam);
		shader = new StaticShader();
		loadStaticCamData(cam);
		instance = this;
	}
	
	private void loadStaticCamData(Camera cam) {
		Log.debug(this, "Updating camera data in shader.");
		shader.start();
		((StaticShader) shader).loadProjectionMatrix(cam);
		shader.stop();
	}

	@Override
	public void onResize(int width, int height) {
		loadStaticCamData(cam);
	}

	@Override
	public void dispose() {
		shader.dispose();
	}

	@Override
	protected void render() {
		if (model == null) return;
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		GL30.glEnable(GL30.GL_CULL_FACE);
		GL30.glCullFace(GL30.GL_BACK);
		
		shader.start();
		((StaticShader) shader).loadViewMatrix(cam);
		
		((StaticShader) shader).loadAmbientLight(Registries.Lights.getAmbientLight());
		((StaticShader) shader).loadDirectionalLight(Registries.Lights.getDirectionalLight());
		((StaticShader) shader).loadCameraPosition(cam);
		
		((StaticShader) shader).loadTransformationMatrix(model.getTransform());
		
		model.getMeshes().forEach(mesh -> {
			
			((StaticShader) shader).loadMaterial(mesh.getMaterial());
			
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

	public void setModel(Model model) {
		if (model != null) Log.debug(this, "Set model to: " + model.getName());
		this.model = model;
	}

}
