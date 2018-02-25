package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVec3 extends Uniform {

	private Vector3f current;
	private boolean used = false;

	public UniformVec3(String name) {
		super(name);
	}

	public void load(Vector3f val) {
		if(!used || !val.equals(current)) {
			GL20.glUniform3f(super.getLocation(), val.x, val.y, val.z);
			used = true;
			current = val;
		}
	}

	@Override
	public String toString() {
		return "UniformVec3{super: " + super.toString() + ", used: " + used + ", current: " + current + "}";
	}

}
