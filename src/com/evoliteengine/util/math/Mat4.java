package com.evoliteengine.util.math;

public class Mat4 {

	public float m00, m01, m02, m03;
	public float m10, m11, m12, m13;
	public float m20, m21, m22, m23;
	public float m30, m31, m32, m33;

	public Mat4 () {
		this.identity();
	}

	private Mat4 identity () {
		this.m00 = 1F;
		this.m01 = 0F;
		this.m02 = 0F;
		this.m03 = 0F;

		this.m10 = 0F;
		this.m11 = 1F;
		this.m12 = 0F;
		this.m13 = 0F;

		this.m20 = 0F;
		this.m21 = 0F;
		this.m22 = 1F;
		this.m23 = 0F;

		this.m30 = 0F;
		this.m31 = 0F;
		this.m32 = 0F;
		this.m33 = 1F;

		return this;

	}


}
