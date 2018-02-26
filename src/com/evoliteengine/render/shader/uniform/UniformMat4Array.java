package com.evoliteengine.render.shader.uniform;

import org.lwjgl.util.vector.Matrix4f;

public class UniformMat4Array extends Uniform {

	private UniformMat4[] matrixUniforms;

	public UniformMat4Array(String name, int size) {
		super(name);
		matrixUniforms = new UniformMat4[size];
		for(int i = 0; i < size; i++)
			matrixUniforms[i] = new UniformMat4(name + "[" + i + "]");
	}

	@Override
	public void storeUniformLocation(int programID) {
		for(UniformMat4 mat4 : matrixUniforms) {
			mat4.storeUniformLocation(programID);
		}
	}

	public void loadMat4Array(Matrix4f[] matrices) {
		for(int i = 0; i < matrixUniforms.length; i++) {
			if(i < matrices.length)
				matrixUniforms[i].load(matrices[i]);
			else
				matrixUniforms[i].load(new Matrix4f());
		}
	}


}
