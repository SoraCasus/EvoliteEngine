package com.evoliteengine.render.models;

import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.texture.ModelTexture;

public class TexturedModel {

	private Vao vao;
	private ModelTexture texture;


	public TexturedModel (Vao vao, ModelTexture texture) {
		this.vao = vao;
		this.texture = texture;
	}

	public Vao getVao () {
		return vao;
	}

	public ModelTexture getTexture () {
		return texture;
	}

}
