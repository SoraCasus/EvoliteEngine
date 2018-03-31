package com.evoliteengine.render.renderers;

import com.evoliteengine.render.entities.Entity;
import com.evoliteengine.render.globjects.Vao;
import com.evoliteengine.render.models.TexturedModel;
import com.evoliteengine.render.shader.ShadowShader;
import com.evoliteengine.util.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader               - the simple shader program being used for the shadow render
	 *                             pass.
	 * @param projectionViewMatrix - the orthographic projection matrix multiplied by the light's
	 *                             "view" matrix.
	 */
	public ShadowMapEntityRenderer (ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders entities to the shadow map. Each model is first bound and then all
	 * of the entities using that model are rendered to the shadow map.
	 *
	 * @param entities - the entities to be rendered to the shadow map.
	 */
	public void render (Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			bindModel(model.getVao());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
			if (model.getTexture().isHasTransparency()) {
				MasterRenderer.disableCulling();
			}
			for (Entity entity : entities.get(model)) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVao().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			if (model.getTexture().isHasTransparency()) {
				MasterRenderer.enableCulling();
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	private void bindModel (Vao vao) {
		vao.bind(0, 1);
	}

	private void prepareInstance (Entity entity) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.mvpMatrix.load(mvpMatrix);
	}

}
