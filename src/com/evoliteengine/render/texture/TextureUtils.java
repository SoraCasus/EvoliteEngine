package com.evoliteengine.render.texture;

import com.evoliteengine.util.EEFile;
import de.matthiasmann.twl.utils.PNGDecoder;

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
			decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load texture: " + file.toString());
		}
		return new TextureData(buffer, width, height);
	}

	public static int loadTextureToGL(TextureData textureData) {
		return 0;
	}

}