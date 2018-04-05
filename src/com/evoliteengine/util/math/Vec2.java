package com.evoliteengine.util.math;

public class Vec2 {

	public float x;
	public float y;

	public Vec2 (float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2 (float s) {
		this(s, s);
	}

	public Vec2 () {
		this(0F);
	}

	public String toString () {
		return "Vec2{x: " + x + ", y: " + y + "}";
	}

}
