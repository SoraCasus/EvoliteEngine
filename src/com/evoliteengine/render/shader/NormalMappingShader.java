package com.evoliteengine.render.shader;

import com.evoliteengine.render.shader.uniform.*;
import com.evoliteengine.util.EEFile;

public class NormalMappingShader extends ShaderProgram {

	public static final int MAX_LIGHTS = 4;

	private static final EEFile SHADER = new EEFile("shaders/normal.esh");

	public UniformMat4 tfMat = new UniformMat4("tfMat");
	public UniformMat4 projMat = new UniformMat4("projMat");
	public UniformMat4 viewMat = new UniformMat4("viewMat");
	public UniformVec3Array lightPositionEyeSpace = new UniformVec3Array("lightPositionEyeSpace", MAX_LIGHTS);
	public UniformVec3Array lightColour = new UniformVec3Array("lightColour", MAX_LIGHTS);
	public UniformVec3Array attenuation = new UniformVec3Array("attenuation", MAX_LIGHTS);
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformVec3 skyColour = new UniformVec3("skyColour");
	public UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	public UniformVec2 offset = new UniformVec2("offset");
	public UniformVec4 plane = new UniformVec4("plane");
	public UniformSampler modelTexture = new UniformSampler("modelTexture");
	public UniformSampler normalMap = new UniformSampler("normalMap");
	public UniformSampler specularMap = new UniformSampler("specularMap");
	public UniformBoolean usesSpecularMap = new UniformBoolean("usesSpecularMap");

	public NormalMappingShader() {
		super(SHADER, "in_position", "in_texCoords", "in_normal", "in_tangent");
		super.storeUniormLocation(tfMat, projMat, viewMat, lightPositionEyeSpace, lightColour, attenuation,
				shineDamper, reflectivity, skyColour, numberOfRows, offset, plane, modelTexture, normalMap, specularMap,
				usesSpecularMap);
	}

}
