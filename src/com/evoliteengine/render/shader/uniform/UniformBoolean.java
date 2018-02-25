package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;

public class UniformBoolean extends Uniform {

	private boolean current;
	private boolean used = false;

	public UniformBoolean (String name) {
		super(name);
	}

	public void load (boolean bool) {
		if (!used || current != bool) {
			GL20.glUniform1f(super.getLocation(), bool ? 1F : 0F);
			used = true;
			current = bool;
		}
	}

	@Override
	public String toString () {
		return "UniformBoolean{super: " + super.toString() + ", current: " + current + ", used: " + used + "}";
	}


}
