package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.util.EEFile;

public class GuiShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/gui.esh");

	public UniformMat4 tfMat = new UniformMat4("tfMat");
	public UniformSampler guiTexture = new UniformSampler("guiTexture");


	public GuiShader () {
		super(SHADER, "in_position");
		super.storeUniormLocation(tfMat, guiTexture);
	}


}
