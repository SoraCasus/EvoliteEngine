package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {

	private static final int NO_INDEX = -1;

	private String name;
	private int location;

	protected Uniform(String name) {
		this.name = name;
	}

	public void storeUniformLocation(int programID) {
		this.location = GL20.glGetUniformLocation(programID, name);
		if(location == NO_INDEX) {
			System.err.println("Could not find Uniform: " + this.toString());
		}
	}

	public int getLocation () {
		return location;
	}

	@Override
	public String toString() {
		return "Uniform{name: " + name + ", location: " + location + "}";
	}

}
