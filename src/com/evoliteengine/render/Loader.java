package com.evoliteengine.render;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.models.RawModel;
import com.evoliteengine.render.texture.TextureData;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import com.evoliteengine.util.EEFile;

public class Loader {

	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();

	@Deprecated(since = "0.2.2", forRemoval = true)
	public RawModel loadToVAO (float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		Vao vao = new Vao();
		vao.bind(0, 1, 2);
		vao.createIndexBuffer(indices);
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.unbind(0, 1, 2);
		return new RawModel(vao);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public int loadToVAO (float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public RawModel loadToVAO (float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		Vao vao = new Vao();
		vao.bind(0, 1, 2, 3);
		vao.createIndexBuffer(indices);
		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		vao.createAttribute(3, tangents, 3);
		vao.unbind(0, 1, 2, 3);

		return new RawModel(vao);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public int createEmptyVbo (int floatCount) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public void addInstanceAttribute (int vao, int vbo, int attribute, int dataSize,
	                                  int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4,
				offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public void updateVbo (int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	public RawModel loadToVAO (float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();

		Vao vao = new Vao();
		vao.bind(0);
		vao.createAttribute(0, positions, dimensions);
		vao.setVertexCount(positions.length / dimensions);

		return new RawModel(vao);
	}

	public int loadTexture (EEFile file) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", file.getInputStream());
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
			if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = Math.min(4f,
						GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
						amount);
			} else {
				System.out.println("Anisotropic filtering not supported");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + file.getName() + ", didn't work");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public int loadTextureAtlas (EEFile file) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", file.getInputStream());
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + file.toString() + ", didn't work");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public void cleanUp () {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private int createVAO () {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private void storeDataInAttributeList (int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private void unbindVAO () {
		GL30.glBindVertexArray(0);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private void bindIndicesBuffer (int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private IntBuffer storeDataInIntBuffer (int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	@Deprecated(since = "0.2.2", forRemoval = true)
	private FloatBuffer storeDataInFloatBuffer (float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public int loadCubeMap (String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(new EEFile("textures/skybox/" + textureFiles[i] + ".png"));
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
					data.getWidth(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	private TextureData decodeTextureFile (EEFile fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try (InputStream in = fileName.getInputStream()) {
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

}
