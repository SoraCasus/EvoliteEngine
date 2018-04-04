package com.evoliteengine.render.renderers;

import com.evoliteengine.render.font.FontType;
import com.evoliteengine.render.font.GUIText;
import com.evoliteengine.render.shader.FontShader;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer () {
		shader = new FontShader();
	}

	public void render (Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			font.getTextureAtlas().bindToUnit(0);
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp () {
		shader.delete();
	}

	private void prepare () {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}

	private void renderText (GUIText text) {
		text.getMesh().bind(0, 1);
		shader.colour.load(text.getColour());
		shader.translation.load(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		text.getMesh().unbind(0, 1);
	}

	private void endRendering () {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
