package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;

public class UniformFloat extends Uniform {

	private float current;
	private boolean used = false;

	public UniformFloat (String name) {
		super(name);
	}

	public void load (float val) {
		if (!used || current != val) {
			GL20.glUniform1f(super.getLocation(), val);
			used = true;
			current = val;
		}
	}

	@Override
	public String toString () {
		return "UniformFloat{super: " + super.toString() + ", current: " + current + ", used: " + used + "}";
	}

}
