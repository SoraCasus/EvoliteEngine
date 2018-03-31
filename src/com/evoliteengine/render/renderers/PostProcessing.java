package com.evoliteengine.render.renderers;

import com.evoliteengine.render.globjects.Vao;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.evoliteengine.render.models.RawModel;
import com.evoliteengine.render.Loader;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static Vao quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static BrightFilterRenderer brightFilter;
	private static CombineFilterRenderer combineFilter;

	public static void init (Loader loader) {
		Vao vao = new Vao();
		vao.bind(0);
		vao.createAttribute(0, POSITIONS, 2);
		vao.setVertexCount(POSITIONS.length / 2);
		vao.unbind(0);
		quad = vao;
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		vBlur = new VerticalBlur(Display.getWidth() / 5, Display.getHeight() / 5);
		brightFilter = new BrightFilterRenderer(Display.getHeight() / 2, Display.getWidth() / 2);
		combineFilter = new CombineFilterRenderer();
	}

	public static void doPostProcessing (int colourTexture, int brightTexture) {
		start();
		brightFilter.render(colourTexture);
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colourTexture, vBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		end();
	}

	public static void cleanUp () {
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
	}

	private static void start () {
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end () {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
