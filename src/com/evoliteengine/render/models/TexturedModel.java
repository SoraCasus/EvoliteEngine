package com.evoliteengine.render.models;

import com.evoliteengine.render.globjects.Vao;

public class TexturedModel {

	private Vao vao;
	private Material material;


	public TexturedModel (Vao vao, Material material) {
		this.vao = vao;
		this.material = material;
	}

	public Vao getVao () {
		return vao;
	}

	public Material getMaterial () {
		return material;
	}

}
