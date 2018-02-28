package com.evoliteengine.render.renderers;

import java.util.List;

import com.evoliteengine.render.models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.evoliteengine.render.DisplayManager;
import com.evoliteengine.render.Loader;
import com.evoliteengine.util.Maths;
import com.evoliteengine.render.entities.Camera;
import com.evoliteengine.render.entities.Light;
import com.evoliteengine.util.EEFile;
import com.evoliteengine.render.water.WaterFrameBuffers;
import com.evoliteengine.render.shader.WaterShader;
import com.evoliteengine.render.water.WaterTile;

public class WaterRenderer {

	private static final EEFile DUDV_MAP = new EEFile("textures/water/dudv.png");
	private static final EEFile NORMAL_MAP = new EEFile("textures/normal/normal.png");
	private static final float WAVE_SPEED = 0.03f;

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;

	private float moveFactor = 0;

	private int dudvTexture;
	private int normalMap;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix,
	                     WaterFrameBuffers fbos) {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.projMat.load(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.modelMat.load(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	private void prepareRender(Camera camera, Light sun) {
		shader.start();
		shader.viewMat.load(Maths.createViewMatrix(camera));
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.moveFactor.load(moveFactor);
		shader.lightColour.load(sun.getColour());
		shader.lightPosition.load(sun.getPosition());
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		shader.reflectionTexture.load(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		shader.refractionTexture.load(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		shader.dudvMap.load(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		shader.normalMap.load(3);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		shader.depthMap.load(4);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
		quad = loader.loadToVAO(vertices, 2);
	}

}
