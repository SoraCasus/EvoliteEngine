package com.evoliteengine.render.renderers;

import java.util.List;
import java.util.Map;

import com.evoliteengine.render.shader.NormalMappingShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.evoliteengine.render.entities.Camera;
import com.evoliteengine.render.entities.Entity;
import com.evoliteengine.render.entities.Light;
import com.evoliteengine.render.models.RawModel;
import com.evoliteengine.render.models.TexturedModel;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.Maths;

public class NormalMappingRenderer {

	private NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.start();
		shader.projMat.load(projectionMatrix);
		shader.modelTexture.load(0);
		shader.normalMap.load(1);
		shader.specularMap.load(2);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.start();
		prepare(clipPlane, lights, camera);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}

	public void cleanUp() {
		shader.delete();
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		ModelTexture texture = model.getTexture();
		shader.numberOfRows.load(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.shineDamper.load(texture.getShineDamper());
		shader.reflectivity.load(texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormalMap());
		shader.usesSpecularMap.load(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
		}
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.tfMat.load(transformationMatrix);
		shader.offset.load(new Vector2f(entity.getTextureXOffset(), entity.getTextureYOffset()));
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.plane.load(clipPlane);
		//need to be public variables in MasterRenderer
		shader.skyColour.load(new Vector3f(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE));
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		Vector3f[] positions = new Vector3f[NormalMappingShader.MAX_LIGHTS];
		Vector3f[] colours = new Vector3f[NormalMappingShader.MAX_LIGHTS];
		Vector3f[] attenuation = new Vector3f[NormalMappingShader.MAX_LIGHTS];
		for (int i = 0; i < NormalMappingShader.MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				Light l = lights.get(i);
				positions[i] = getEyeSpacePosition(l.getPosition(), viewMatrix);
				colours[i] = l.getColour();
				attenuation[i] = l.getAttenuation();
			} else {
				positions[i] = new Vector3f();
				colours[i] = new Vector3f();
				attenuation[i] = new Vector3f(1, 0, 0);
			}
		}

		shader.lightPositionEyeSpace.load(positions);
		shader.lightColour.load(colours);
		shader.attenuation.load(attenuation);
		shader.viewMat.load(viewMatrix);
	}

	private Vector3f getEyeSpacePosition(Vector3f light, Matrix4f viewMatrix) {
		Vector4f eyeSpacePos = new Vector4f(light.x, light.y, light.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}

}
