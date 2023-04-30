package client.rendering.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import client.rendering.cameras.Camera;
import client.rendering.shaders.StaticShader;
import client.rendering.utils.Loader;

public class StaticRenderer extends BaseRenderer {
	
	float[] vertices = 
		{ -1000f, 1000f, 0,
		  -1000f, -1000f, 0,
		  1000f, 1000f, 0
		};
	float[] normals = 
		{ 0, 0, 1,
		  0, 0, 1,
		  0, 0, 1
		};
	float[] tc = 
		{
			0, 1,
			0, 1,
			0, 1
		};
	int[] indices = {
			0, 1, 2
	};
	int vao;
	
	public StaticRenderer(Camera cam) {
		super(cam);
		shader = new StaticShader();
		vao = Loader.loadModelData(vertices, tc, normals, indices);
	}

	@Override
	protected void render() {
		//shader.start();
		
		/*RenderManager.getStaticModels().forEach(model -> {
			
			model.getMeshes().forEach(mesh -> {
				
				GL30.glBindVertexArray(mesh.getVaoId());
				
				shader.loadTransformationMatrix(model.getTransform());
				
				GL30.glDrawElements(GL11.GL_TRIANGLES, mesh.getNumberOfIndices(), GL11.GL_UNSIGNED_INT, 0);
				
				GL30.glBindVertexArray(0);
				
			});
		}) ;*/
		
		GL30.glBindVertexArray(vao);
		
		GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, indices.length);
		
		GL30.glBindVertexArray(0);
		
		//shader.stop();
	}

	@Override
	protected void dispose() {
		shader.dispose();
	}
	
	

}
