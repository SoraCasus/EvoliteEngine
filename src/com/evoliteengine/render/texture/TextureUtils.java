package com.evoliteengine.render.texture;

import com.evoliteengine.util.EEFile;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureUtils {

	public static TextureData decodeTextureFile (EEFile file) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try (InputStream in = file.getInputStream()) {
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			// 4 Colour Channels Red, Green, Blue, Alpha
			// total pixels in image = width * height
			// 1 byte / channel = 4 bytes per pixel
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			// stride = width of image in bytes, 4 channels / pixel
			decoder.decode(buffer, width * 4, PNGDecoder.Format.BGRA);
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load texture: " + file.toString());
		}
		return new TextureData(buffer, width, height);
	}

	public static int loadTextureToGL(TextureData textureData, TextureBuilder builder) {

		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(
				GL11.GL_TEXTURE_2D,         // GLenum           target,
				0,					    // GLint            level,
				GL11.GL_RGBA,		        // GLint            internalFormat,
				textureData.getWidth(),     // GLsizei          width,
				textureData.getHeight(),	// GLsizei          height,
				0,					// GLint            border,
				GL12.GL_BGRA,				// GLenum           format,
				GL11.GL_UNSIGNED_BYTE,		// GLenum           type,
 	            textureData.getBuffer()     // const GLvoid*    data
		);

		if(builder.isMipmap()) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

			if(builder.isAnisotropic() && GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0F);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 4.0F);
			}
		} else if (builder.isNearest()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(builder.isClampEdges()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return texID;
	}

}