package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.UniformVec2;
import com.evoliteengine.render.shader.uniform.UniformVec3;
import com.evoliteengine.util.EEFile;


public class FontShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/font.esh");

	public UniformVec3 colour = new UniformVec3("colour");
	public UniformVec2 translation = new UniformVec2("translation");

	public FontShader () {
		super(SHADER, "in_position", "in_texCoords");
		super.storeUniormLocation(colour, translation);
	}

}
