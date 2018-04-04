package com.evoliteengine.render.texture;

import com.evoliteengine.util.EEFile;

public class TextureBuilder {

	private boolean clampEdges;
	private boolean mipmap;
	private boolean anisotropic;
	private boolean nearest;

	private EEFile file;

	public TextureBuilder(EEFile file) {
		this.file = file;
		this.clampEdges = false;
		this.mipmap = false;
		this.anisotropic = true;
		this.nearest = false;
	}

	public Texture create() {
		TextureData data = TextureUtils.decodeTextureFile(file);
		int textureID = TextureUtils.loadTextureToGL(data, this);
		return new Texture(textureID, data.getWidth(), file);
	}

	public TextureBuilder clampEdges() {
		this.clampEdges = true;
		return this;
	}

	public TextureBuilder normalMipmap() {
		this.mipmap = true;
		this.anisotropic = false;
		return this;
	}

	public TextureBuilder nearestFiltering() {
		this.mipmap = false;
		this.anisotropic = false;
		this.nearest = true;
		return this;
	}

	public TextureBuilder anisotropic() {
		this.mipmap = true;
		this.anisotropic = true;
		return this;
	}

	public boolean isClampEdges() {
		return clampEdges;
	}

	public boolean isMipmap() {
		return mipmap;
	}

	public boolean isAnisotropic() {
		return anisotropic;
	}

	public boolean isNearest() {
		return nearest;
	}
}
