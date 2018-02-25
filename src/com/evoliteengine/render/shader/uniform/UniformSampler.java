package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;

public class UniformSampler extends Uniform {

	private boolean used = false;
	private int current;

	public UniformSampler(String name) {
		super(name);
	}

	public void load(int val) {
		if(!used || val != current) {
			GL20.glUniform1i(super.getLocation(), val);
			used = true;
			current = val;
		}
	}

	@Override
	public String toString() {
		return "UniformSampler{super: " + super.toString() + ", used: " + used + ", current: " + current + "}";
	}

}
