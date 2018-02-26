package com.evoliteengine.render.renderers;

import com.evoliteengine.render.shader.VerticalBlurShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

public class VerticalBlur {
	
	private ImageRenderer renderer;
	private VerticalBlurShader shader;
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight){
		shader = new VerticalBlurShader();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.targetHeight.load(targetFboHeight);
		shader.stop();
	}

	
	public void render(int texture){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		shader.originalTexture.load(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.delete();
	}
}
