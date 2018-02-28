package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class ContrastShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/contrast.esh");

	public UniformSampler colourTexture = new UniformSampler("colourTexture");

	public ContrastShader() {
		super(SHADER, "in_Position");
		super.storeUniormLocation(colourTexture);
	}

}
