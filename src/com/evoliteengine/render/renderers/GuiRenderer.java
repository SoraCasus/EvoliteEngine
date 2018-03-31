package com.evoliteengine.render.renderers;

import com.evoliteengine.render.Loader;
import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.shader.GuiShader;
import com.evoliteengine.render.texture.GuiTexture;
import com.evoliteengine.util.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class GuiRenderer {

	private final Vao quad;
	private GuiShader shader;

	public GuiRenderer (Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		Vao vao = new Vao();
		vao.bind(0);
		vao.createAttribute(0, positions, 2);
		vao.setVertexCount(positions.length / 2);
		vao.unbind(0);
		quad = vao;
		shader = new GuiShader();
	}

	public void render (List<GuiTexture> guis) {
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			shader.guiTexture.load(0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale(), gui.getRotation());
			shader.tfMat.load(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp () {
		shader.delete();
	}
}
