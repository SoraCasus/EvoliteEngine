package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class HorizontalBlurShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/horizontalBlur.esh");

	public UniformFloat targetWidth = new UniformFloat("targetWidth");
	public UniformSampler originalTexture = new UniformSampler("originalTexture");

	public HorizontalBlurShader() {
		super(SHADER, "in_position");
		super.storeUniormLocation(targetWidth, originalTexture);
	}
}
