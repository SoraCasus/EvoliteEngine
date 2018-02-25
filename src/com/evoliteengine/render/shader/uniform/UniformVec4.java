package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class UniformVec4 extends Uniform {

	private Vector4f current;
	private boolean used = false;

	public UniformVec4 (String name) {
		super(name);
	}

	public void load (Vector4f val) {
		if (!used || !val.equals(current)) {
			GL20.glUniform2f(super.getLocation(), val.x, val.y);
			used = true;
			current = val;
		}
	}

	@Override
	public String toString () {
		return "UniformVec4{super: " + super.toString() + ", used: " + used + ", current: " + current + "}";
	}


}
