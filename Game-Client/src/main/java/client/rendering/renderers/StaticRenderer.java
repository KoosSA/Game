package client.rendering.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import client.rendering.cameras.Camera;
import client.rendering.utils.RenderManager;

public class StaticRenderer extends BaseRenderer {

	public StaticRenderer(Camera cam) {
		super(cam);
	}

	@Override
	protected void render() {
		RenderManager.getStaticModels().forEach(model -> {
			
			model.getMeshes().forEach(mesh -> {
				
				GL30.glBindVertexArray(mesh.getVaoId());
				
				GL30.glDrawElements(GL11.GL_TRIANGLES, mesh.getNumberOfIndices(), GL11.GL_UNSIGNED_INT, 0);
				
				GL30.glBindVertexArray(0);
				
			});
			
		}) ;
	}

	@Override
	protected void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	

}
