package com.evoliteengine.render.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evoliteengine.render.font.FontType;
import com.evoliteengine.render.font.GUIText;
import com.evoliteengine.render.font.TextMeshData;
import com.evoliteengine.render.renderers.FontRenderer;
import renderEngine.Loader;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;

	public static void init (Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}

	public static void render () {
		renderer.render(texts);
	}

	public static void loadText (GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
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
