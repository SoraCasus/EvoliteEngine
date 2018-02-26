package com.evoliteengine.render.shader;


import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class BrightFilterShader extends ShaderProgram {
	
	private static final EEFile SHADER = new EEFile("shaders/brightFilter.esh");

	public UniformSampler colourTexture = new UniformSampler("colourTexture");

	public BrightFilterShader() {
		super(SHADER, "in_position");
		super.storeUniormLocation(colourTexture);
	}

}
