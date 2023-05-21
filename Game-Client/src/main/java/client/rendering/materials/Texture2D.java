package client.rendering.materials;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class Texture2D {
	
	private int id;
	private String name;
	
	public Texture2D(String name) {
		this.name = name;
		String path = Files.getCommonFolderPath(CommonFolders.Textures) + "/" + name;
		createTexture(path);
	}

	private void createTexture(String path) {
		Log.debug(this, "Creating texture: " + name);
		
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer data = STBImage.stbi_load(path, w, h, c, STBImage.STBI_rgb_alpha);
		if (data == null) {
			Log.error(this, "Failed to load image: " + path);
			return;
		}
		
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL30.GL_TEXTURE_2D, id);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA16, w[0], h[0], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		STBImage.stbi_image_free(data);
		
		int result = GL30.GL_NO_ERROR;
		while ((result = GL30.glGetError()) != GL30.GL_NO_ERROR) {
			Log.error(this, "Opengl Error: " + result);
		}
	}
	
	public void dispose() {
		GL30.glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}

}
