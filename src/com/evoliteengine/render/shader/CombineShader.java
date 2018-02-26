package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class CombineShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/combineFilter.esh");

	public UniformSampler colourTexture = new UniformSampler("colourTexture");
	public UniformSampler highlightTexture = new UniformSampler("highlightTexture");

	public CombineShader () {
		super(SHADER, "in_position");
		super.storeUniormLocation(colourTexture, highlightTexture);
	}

}

