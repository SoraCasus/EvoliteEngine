package com.evoliteengine.render.shader.uniform;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVec2 extends Uniform {

	 private Vector2f current;
	 private boolean used = false;

	 public UniformVec2(String name) {
	 	super(name);
	 }

	 public void load(Vector2f val) {
	 	 if(!used || !val.equals(current)) {
		     GL20.glUniform2f(super.getLocation(), val.x, val.y);
		     used = true;
		     current = val;
	     }
	 }

	 @Override
	 public String toString() {
	 	return "UniformVec2{super: " + super.toString() + ", used: " + used + ", current: " + current + "}";
	 }



}
