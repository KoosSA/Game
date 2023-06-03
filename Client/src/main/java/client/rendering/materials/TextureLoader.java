package client.rendering.materials;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class TextureLoader {
	
	public static Texture2D createTexture(String name) {
		String path = Files.getCommonFolderPath(CommonFolders.Textures) + "/" + name;
		Log.debug(TextureLoader.class, "Creating texture: " + name);
		Log.debug(TextureLoader.class, path);
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);;
		IntBuffer c = BufferUtils.createIntBuffer(1);;
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer data = STBImage.stbi_load(path, w, h, c, 0);
		if (data == null || data.limit() <= 0) {
			Log.error(TextureLoader.class, "Failed to load image: " + path + " with error: " + STBImage.stbi_failure_reason());
			return null;
		}
		
		//Clearing opengl errors to detect only texture loading errors.
		int result = GL30.GL_NO_ERROR;
		while ((result = GL30.glGetError()) != GL30.GL_NO_ERROR) {
			Log.error(TextureLoader.class, "Opengl Error: " + result);
			Log.error(TextureLoader.class, "This error probably has nothing to do with the texture currently being loaded to opengl");
		}
		
		int id = GL45.glGenTextures();
		GL45.glActiveTexture(GL45.GL_TEXTURE0);
		GL45.glBindTexture(GL30.GL_TEXTURE_2D, id);
		
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_S, GL45.GL_REPEAT);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_T, GL45.GL_REPEAT);
		
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_LINEAR);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_LINEAR);
		
		GL45.glPixelStorei(GL45.GL_UNPACK_ALIGNMENT, 1);
		int gltextype = getGlTexType(c.get(0));
		GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, w.get(0), h.get(0), 0, gltextype, GL45.GL_UNSIGNED_BYTE, data);
		GL45.glGenerateMipmap(GL45.GL_TEXTURE_2D);
		
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
		
		//Detecting opengl texture loading errors.
		result = GL30.GL_NO_ERROR;
		boolean hasErr = false;
		while ((result = GL30.glGetError()) != GL30.GL_NO_ERROR) {
			Log.error(TextureLoader.class, "Opengl Error while loading textures: " + result);
			hasErr = true;
			Log.error(TextureLoader.class, "Texture file has channel num of: " + c.get(0));
		}
		
		STBImage.stbi_image_free(data);
		if (hasErr) {
			Log.error(TextureLoader.class, "OpenGL error detected, deleting texture with error.");
			GL30.glDeleteTextures(id);
			return null;
		}
		Texture2D texture = new Texture2D(id, w.get(0), h.get(0), c.get(0), name);
		return texture;
	}

	private static int getGlTexType(int ch) {
		switch (ch) {
		case 1: return GL30.GL_ALPHA;
		case 2:	return GL30.GL_RG;
		case 3: return GL30.GL_RGB;
		case 4: return GL30.GL_RGBA;
		default:
			return GL30.GL_RGB;
		}
	}

}
