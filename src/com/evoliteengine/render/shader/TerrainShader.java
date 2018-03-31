package com.evoliteengine.render.shader;


import com.evoliteengine.render.shader.uniform.UniformFloat;
import com.evoliteengine.render.shader.uniform.UniformMat4;
import com.evoliteengine.render.shader.uniform.UniformSampler;
import com.evoliteengine.render.shader.uniform.UniformVec3;
import com.evoliteengine.render.shader.uniform.UniformVec3Array;
import com.evoliteengine.render.shader.uniform.UniformVec4;
import com.evoliteengine.util.EEFile;

public class TerrainShader extends ShaderProgram {

	public static final int MAX_LIGHTS = 4;

	private static final EEFile SHADER = new EEFile("shaders/terrain.esh");

	public UniformMat4 tfMat = new UniformMat4("tfMat");
	public UniformMat4 projMat = new UniformMat4("projMat");
	public UniformMat4 viewMat = new UniformMat4("viewMat");
	public UniformVec3Array lightPosition = new UniformVec3Array("lightPosition", MAX_LIGHTS);
	public UniformVec3Array lightColour = new UniformVec3Array("lightColour", MAX_LIGHTS);
	public UniformVec3Array attenuation = new UniformVec3Array("attenuation", MAX_LIGHTS);
	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	public UniformVec3 skyColour = new UniformVec3("skyColour");
	public UniformSampler bgTexture = new UniformSampler("bgTexture");
	public UniformSampler rTexture = new UniformSampler("rTexture");
	public UniformSampler gTexture = new UniformSampler("gTexture");
	public UniformSampler bTexture = new UniformSampler("bTexture");
	public UniformSampler blendMap = new UniformSampler("blendMap");
	public UniformVec4 plane = new UniformVec4("plane");
	public UniformMat4 toShadowMapSpace = new UniformMat4("toShadowMapSpace");
	public UniformSampler shadowMap = new UniformSampler("shadowMap");

	public TerrainShader() {
		super(SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeUniormLocation(tfMat, projMat, viewMat, lightPosition, lightColour,
				attenuation, shineDamper, reflectivity, skyColour, bgTexture, rTexture,
				gTexture, bTexture, blendMap, plane, toShadowMapSpace, shadowMap);
	}


}
