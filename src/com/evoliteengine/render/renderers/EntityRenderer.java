package com.evoliteengine.render.renderers;

import java.util.List;
import java.util.Map;

import com.evoliteengine.render.models.RawModel;
import com.evoliteengine.render.models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector2f;
import com.evoliteengine.render.shader.StaticShader;
import com.evoliteengine.render.texture.ModelTexture;
import com.evoliteengine.util.Maths;
import com.evoliteengine.render.entities.Entity;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projMat.load(projectionMatrix);

		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowMapSpace) {
		shader.toShadowMapSpace.load(toShadowMapSpace);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.numberOfRows.load(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.useFakeLighting.load(texture.isUseFakeLighting());
		shader.shineDamper.load(texture.getShineDamper());
		shader.reflectivity.load(texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		shader.modelTexture.load(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		shader.usesSpecularMap.load(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			shader.specularMap.load(1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
		}
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.tfMat.load(transformationMatrix);
		shader.offset.load(new Vector2f(entity.getTextureXOffset(), entity.getTextureYOffset()));
	}

}
