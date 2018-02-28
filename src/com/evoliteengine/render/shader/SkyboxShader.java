package com.evoliteengine.render.shader;


import com.evoliteengine.render.DisplayManager;
import com.evoliteengine.render.entities.Camera;
import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.render.shader.uniform.UniformVec3;
import com.evoliteengine.util.EEFile;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import com.evoliteengine.util.Maths;

public class SkyboxShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/skybox.esh");

	private static final float ROTATE_SPEED = 0.5f;

	public UniformMat4 projMat = new UniformMat4("projMat");
	public UniformMat4 viewMat = new UniformMat4("viewMat");
	public UniformVec3 fogColour = new UniformVec3("fogColour");
	public UniformSampler cubeMap = new UniformSampler("cubeMap");
	public UniformSampler cubeMap2 = new UniformSampler("cubeMap2");
	public UniformFloat blendFactor = new UniformFloat("blendFactor");
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(SHADER, "in_position");
		super.storeUniormLocation(projMat, viewMat, fogColour, cubeMap,
				cubeMap2, blendFactor);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		matrix.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0));
		this.viewMat.load(matrix);
	}

}
