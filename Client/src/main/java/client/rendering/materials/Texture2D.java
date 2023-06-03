package client.rendering.materials;

import org.lwjgl.opengl.GL30;

public class Texture2D {
	
	private int id, width, height, channels;
	private String name;
	
	public Texture2D(int id, int width, int height, int channels, String name) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.name = name;
	}

	public void dispose() {
		GL30.glDeleteTextures(id);
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getChannels() {
		return channels;
	}

	public String getName() {
		return name;
	}
	
	

}
