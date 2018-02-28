package com.evoliteengine.render.shader;


import com.evoliteengine.render.shader.ShaderProgram;
import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.render.shader.uniform.UniformVec3;
import com.evoliteengine.util.EEFile;

public class WaterShader extends ShaderProgram {

	private static final EEFile SHADER = new EEFile("shaders/water.esh");

	public UniformMat4 modelMat = new UniformMat4("modelMat");
	public UniformMat4 viewMat = new UniformMat4("viewMat");
	public UniformMat4 projMat = new UniformMat4("projMat");
	public UniformSampler reflectionTexture = new UniformSampler("reflectionTexture");
	public UniformSampler refractionTexture = new UniformSampler("refractionTexture");
	public UniformSampler dudvMap = new UniformSampler("dudvMap");
	public UniformFloat moveFactor = new UniformFloat("moveFactor");
	public UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	public UniformSampler normalMap = new UniformSampler("normalMap");
	public UniformVec3 lightColour = new UniformVec3("lightColour");
	public UniformVec3 lightPosition = new UniformVec3("lightPosition");
	public UniformSampler depthMap = new UniformSampler("depthMap");

	public WaterShader() {
		super(SHADER, "in_position");
		super.storeUniormLocation(modelMat, viewMat, projMat, reflectionTexture, refractionTexture,
				dudvMap, moveFactor, cameraPosition, normalMap, lightColour, lightPosition, depthMap);

	}
}
