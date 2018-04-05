package com.evoliteengine.util.math;

public class Vec3 {

	public float x;
	public float y;
	public float z;

	public Vec3 (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 (Vec2 xy, float z) {
		this(xy.x, xy.y, z);
	}

	public Vec3 (float x, Vec2 yz) {
		this(x, yz.x, yz.y);
	}

	public Vec3 (Vec2 xy) {
		this(xy, 0F);
	}

	public Vec3 (float s) {
		this(s, s, s);
	}

	public Vec3 () {
		this(0F);
	}


	public String toString () {
		return "Vec3{x: " + x + ", y: " + y + ", z: " + z + "}";
	}

}
