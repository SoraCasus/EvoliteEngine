package com.evoliteengine.render.models;

import com.evoliteengine.render.texture.Texture;
import com.evoliteengine.util.EEFile;

public class Material {

	private static final Texture NULL_TEXTURE;

	static {
		NULL_TEXTURE = Texture.newTexture(new EEFile("textures/diffuse/NULL_TEXTURE.png")).create();
	}

	private Texture diffuse;
	private Texture normal;
	private Texture specular;
	private boolean transparent;
	private boolean useFakeLighting;
	private int numberOfRows;
	private float shineDamper;
	private float reflectivity;

	public Material() {
		this.transparent = false;
		this.numberOfRows = 1;
		this.shineDamper = 1;
		this.reflectivity = 0;
		this.diffuse = NULL_TEXTURE;
		this.normal = null;
		this.specular = null;
		this.useFakeLighting = false;

	}

	public static Texture getNullTexture() {
		return NULL_TEXTURE;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public Material setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
		return this;
	}

	public Texture getSpecular() {
		return specular;
	}

	public Material setSpecular(Texture specular) {
		this.specular = specular;
		return this;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public Material setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
		return this;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public Material setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public Material setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
		return this;
	}

	public Texture getDiffuse() {
		return diffuse;
	}

	public Material setDiffuse(Texture diffuse) {
		this.diffuse = diffuse;
		return this;
	}

	public Texture getNormal() {
		return normal;
	}

	public Material setNormal(Texture normal) {
		this.normal = normal;
		return this;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public Material setTransparent(boolean transparent) {
		this.transparent = transparent;
		return this;
	}
}
