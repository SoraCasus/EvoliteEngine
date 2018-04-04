package com.evoliteengine.render.renderers;

import com.evoliteengine.render.DisplayManager;
import com.evoliteengine.render.entities.Camera;
import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.shader.SkyboxShader;
import com.evoliteengine.render.texture.Texture;
import com.evoliteengine.util.EEFile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxRenderer {
	private static final float SIZE = 500f;

	private static final float[] VERTICES = {
			-SIZE, SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, SIZE,

			-SIZE, SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, SIZE
	};

	private Vao cube;
	private Texture texture;
	private Texture nightTexture;
	private SkyboxShader shader;
	private float time = 0;

	public SkyboxRenderer (Matrix4f projectionMatrix) {
		Vao vao = new Vao();
		vao.bind(0);
		vao.createAttribute(0, VERTICES, 3);
		vao.setVertexCount(VERTICES.length / 3);
		vao.unbind(0);
		cube = vao;
		texture = Texture.newCubeMap(new EEFile("textures/skybox/day"));
		nightTexture = Texture.newCubeMap(new EEFile("textures/skybox/night"));
		shader = new SkyboxShader();
		shader.start();
		shader.cubeMap.load(0);
		shader.cubeMap2.load(1);
		shader.projMat.load(projectionMatrix);
		shader.stop();
	}

	public void render (Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.fogColour.load(new Vector3f(r, g, b));
		cube.bind(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures () {
		time += DisplayManager.getFrameTimeSeconds() * 100;
		time %= 24000;
		Texture texture1;
		Texture texture2;
		float blendFactor;
		if (time >= 0 && time < 5000) {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0) / (5000);
		} else if (time >= 5000 && time < 8000) {
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5000) / (8000 - 5000);
		} else if (time >= 8000 && time < 21000) {
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - 8000) / (21000 - 8000);
		} else {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 21000) / (24000 - 21000);
		}

		texture1.bindToUnit(0);
		texture2.bindToUnit(1);

		shader.blendFactor.load(blendFactor);
	}
}
