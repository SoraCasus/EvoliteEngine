package com.evoliteengine.render.shader.uniform;

import org.lwjgl.util.vector.Vector3f;

public class UniformVec3Array extends Uniform {

	private UniformVec3[] uniforms;

	public UniformVec3Array(String name, int size) {
		super(name);
		uniforms = new UniformVec3[size];
		for (int i = 0; i < size; i++)
			uniforms[i] = new UniformVec3(name + "[" + i + "]");
	}

	@Override
	public void storeUniformLocation(int programID) {
		for (UniformVec3 uniform : uniforms)
			uniform.storeUniformLocation(programID);
	}

	public void load(Vector3f[] vecs) {
		for (int i = 0; i < uniforms.length; i++) {
			if (i < vecs.length)
				uniforms[i].load(vecs[i]);
			else
				uniforms[i].load(new Vector3f(0, 0, 0));
		}
	}

}
