package com.evoliteengine.render.shader;


import com.evoliteengine.render.shader.ShaderProgram;
import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.util.EEFile;
import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/particles.esh");

	public UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	public UniformMat4 projMat = new UniformMat4("projMat");

	public ParticleShader() {
		super(SHADER);
		super.storeUniormLocation(numberOfRows, projMat);
		super.bindAttribute("in_position", 0, false);
		super.bindAttribute("in_mvMat", 1, false);
		super.bindAttribute("in_texOffsets", 5, false);
		super.bindAttribute("in_blendFactor", 6, true);
	}

}
