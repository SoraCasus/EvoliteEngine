package com.evoliteengine.render.renderers;

import com.evoliteengine.render.entities.Entity;
import com.evoliteengine.render.models.Material;
import com.evoliteengine.render.models.TexturedModel;
import com.evoliteengine.render.shader.StaticShader;
import com.evoliteengine.util.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer (StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projMat.load(projectionMatrix);

		shader.stop();
	}

	public void render (Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowMapSpace) {
		shader.toShadowMapSpace.load(toShadowMapSpace);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVao().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel (TexturedModel model) {

		model.getVao().bind(0, 1, 2);
		Material material = model.getMaterial();
		shader.numberOfRows.load(material.getNumberOfRows());
		if (material.isTransparent())
			MasterRenderer.disableCulling();

		shader.useFakeLighting.load(material.isUseFakeLighting());
		shader.shineDamper.load(material.getShineDamper());
		shader.reflectivity.load(material.getReflectivity());

		shader.modelTexture.load(0);
		material.getDiffuse().bindToUnit(0);

		boolean hasSpecularMap = material.getSpecular() != null;
		shader.usesSpecularMap.load(hasSpecularMap);
		if (hasSpecularMap) {
			material.getSpecular().bindToUnit(1);
			shader.specularMap.load(1);
		}
	}

	private void unbindTexturedModel () {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance (Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.tfMat.load(transformationMatrix);
		shader.offset.load(new Vector2f(entity.getTextureXOffset(), entity.getTextureYOffset()));
	}

}
