package com.evoliteengine.render.globjects;

import com.evoliteengine.core.IDisposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import java.util.ArrayList;
import java.util.List;

public class Vao implements IDisposable {

	private static final int BYTES_PER_FLOAT = 4;
	public final int vaoID;
	private List<Vbo> dataVbos;
	private Vbo indexVbo;
	private int vertexCount;

	public Vao () {
		this.vaoID = GL30.glGenVertexArrays();
		this.dataVbos = new ArrayList<>();
	}

	public void bind (int... attribs) {
		bind();
		if(indexVbo != null) indexVbo.bind();
		for (int i : attribs)
			GL20.glEnableVertexAttribArray(i);
	}

	public void unbind (int... attribs) {
		for (int i : attribs)
			GL20.glDisableVertexAttribArray(i);
		unbind();
	}

	public void createIndexBuffer (int[] indices) {
		this.indexVbo = new Vbo(GL15.GL_ELEMENT_ARRAY_BUFFER);
		this.indexVbo.bind();
		this.indexVbo.storeData(indices);
		this.vertexCount = indices.length;
	}

	public void createAttribute (int attrib, float[] data, int attribSize) {
		Vbo dataVbo = new Vbo(GL15.GL_ARRAY_BUFFER);
		dataVbo.bind();
		dataVbo.storeData(data);
		GL20.glVertexAttribPointer(attrib, attribSize, GL11.GL_FLOAT, false, attribSize * BYTES_PER_FLOAT, 0);
		dataVbo.unbind();
		dataVbos.add(dataVbo);
	}

	public void createAttribute (int attrib, int[] data, int attribSize) {
		Vbo dataVbo = new Vbo(GL15.GL_ARRAY_BUFFER);
		dataVbo.bind();
		dataVbo.storeData(data);
		GL30.glVertexAttribIPointer(attrib, attribSize, GL11.GL_INT, attribSize * BYTES_PER_FLOAT, 0);
		dataVbo.unbind();
		dataVbos.add(dataVbo);
	}

	@Override
	public void delete () {
		GL30.glDeleteVertexArrays(vaoID);
		if (indexVbo != null) indexVbo.delete();
		for(Vbo vbo : dataVbos)
			vbo.delete();
	}

	private void bind () {
		GL30.glBindVertexArray(this.vaoID);
	}

	private void unbind () {
		GL30.glBindVertexArray(0);
	}

	public void setVertexCount (int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public int getVertexCount () {
		return vertexCount;
	}
}
