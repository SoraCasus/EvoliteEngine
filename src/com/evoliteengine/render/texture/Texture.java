package com.evoliteengine.render.texture;

import com.evoliteengine.core.IDisposable;
import com.evoliteengine.util.EEFile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture implements IDisposable {

	private final int textureID;
	private final int size;
	private final int type;

	private final EEFile file;

	public Texture(int textureID, int size, int type, EEFile file) {
		this.textureID = textureID;
		this.size = size;
		this.type = type;
		this.file = file;
	}

	public Texture(int textureID, int size, EEFile file) {
		this(textureID, size, GL11.GL_TEXTURE_2D, file);
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, textureID);
	}

	@Override
	public void delete() {
		GL11.glDeleteTextures(textureID);
	}

	public static TextureBuilder newTexture(EEFile texture) {
		return new TextureBuilder(texture);
	}

	public static Texture newCubeMap (EEFile directory) {
		int texID = TextureUtils.loadCubeMap(directory);
		return new Texture(texID, 0, GL13.GL_TEXTURE_CUBE_MAP, directory);
	}


	public int getTextureID() {
		return textureID;
	}

	public int getSize() {
		return size;
	}

	public int getType() {
		return type;
	}

	public EEFile getFile() {
		return file;
	}
}
