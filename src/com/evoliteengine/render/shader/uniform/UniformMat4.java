package com.evoliteengine.render.shader.uniform;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

public class UniformMat4 extends Uniform {

	private static FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
	private Matrix4f current;

	public UniformMat4(String name) {
		super(name);
	}

	public void load(Matrix4f mat) {
		mat.store(buffer);
		buffer.flip();
		GL20.glUniformMatrix4(super.getLocation(), false, buffer);
		current = mat;
	}

	@Override
	public String toString () {
		return "UniformMat4{super: " + super.toString() + ", current: " + current + "}";
	}

}
