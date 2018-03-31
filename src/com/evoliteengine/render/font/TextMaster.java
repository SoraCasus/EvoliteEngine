package com.evoliteengine.render.font;

import com.evoliteengine.render.Loader;
import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.renderers.FontRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMaster {


	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;

	public static void init () {
		renderer = new FontRenderer();
	}

	public static void render () {
		renderer.render(texts);
	}

	public static void loadText (GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		// int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		Vao vao = new Vao();
		vao.bind(0, 1);
		vao.createAttribute(0, data.getVertexPositions(), 2);
		vao.createAttribute(1, data.getTextureCoords(), 2);
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());
		textBatch.add(text);
	}

	public static void removeText (GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static void cleanUp () {
		renderer.cleanUp();
	}

}
