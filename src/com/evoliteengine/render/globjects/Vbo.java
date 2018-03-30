package com.evoliteengine.render.globjects;

import com.evoliteengine.core.IDisposable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Vbo implements IDisposable {

	private final int vboID;
	private final int type;

	public Vbo (int type) {
		this.vboID = GL15.glGenBuffers();
		this.type = type;
	}

	public void bind () {
		GL15.glBindBuffer(this.type, this.vboID);
	}

	public void unbind() {
		GL15.glBindBuffer(this.type, 0);
	}

	public void allocateSpace (int floatCount) {
		GL15.glBufferData(this.type, floatCount * 4, GL15.GL_STREAM_DRAW);
	}

	public void storeDynamic(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bind();
		GL15.glBufferData(this.type, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(this.type, 0, buffer);
		unbind();
	}

	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(this.type, buffer, GL15.GL_STATIC_DRAW);
	}

	public void storeData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(this.type, buffer, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void delete() {
		GL15.glDeleteBuffers(vboID);
	}

}
