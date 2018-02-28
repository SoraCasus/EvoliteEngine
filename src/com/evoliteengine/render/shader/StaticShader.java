package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.*;
import com.evoliteengine.util.EEFile;

public class StaticShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private static final EEFile SHADER = new EEFile("shaders/basic.esh");

	public UniformMat4 tfMat = new UniformMat4("tfMat");
	public UniformMat4 projMat = new UniformMat4("projMat");
	public UniformMat4 viewMat = new UniformMat4("viewMat");
	public UniformVec3Array lightPos = new UniformVec3Array("lightPos", MAX_LIGHTS);
	public UniformVec3Array lightColour = new UniformVec3Array("lightColour", MAX_LIGHTS);
	public UniformVec3Array attenuation = new UniformVec3Array("attenuation", MAX_LIGHTS);
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	public UniformVec3 skyColour = new UniformVec3("skyColour");
	public UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	public UniformVec2 offset = new UniformVec2("offset");
	public UniformVec4 plane = new UniformVec4("plane");
	public UniformSampler specularMap = new UniformSampler("specularMap");
	public UniformBoolean usesSpecularMap = new UniformBoolean("usesSpecularMap");
	public UniformSampler modelTexture = new UniformSampler("modelTexture");
	public UniformMat4 toShadowMapSpace = new UniformMat4("toShadowMapSpace");
	public UniformSampler shadowMap = new UniformSampler("shadowMap");

	public StaticShader() {
		super(SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeUniormLocation(tfMat, projMat, viewMat, lightPos, lightColour, attenuation,
				shineDamper, reflectivity, useFakeLighting, skyColour, numberOfRows, offset,
				plane, specularMap, usesSpecularMap, modelTexture, toShadowMapSpace, shadowMap);
	}

}
