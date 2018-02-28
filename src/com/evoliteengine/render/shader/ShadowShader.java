package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.util.EEFile;

public class ShadowShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/shadow.esh");

	public UniformMat4 mvpMatrix = new UniformMat4("mvpMatrix");

	public ShadowShader() {
		super(SHADER, "in_position", "in_textureCoords");
		super.storeUniormLocation(mvpMatrix);
	}

}
