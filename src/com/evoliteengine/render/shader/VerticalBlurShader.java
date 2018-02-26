package com.evoliteengine.render.shader;


import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class VerticalBlurShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/verticalBlur.esh");

	public UniformFloat targetHeight = new UniformFloat("targetHeight");
	public UniformSampler originalTexture = new UniformSampler("originalTexture");

	public VerticalBlurShader () {
		super(SHADER, "in_position");
		super.storeUniormLocation(targetHeight, originalTexture);
	}
}
