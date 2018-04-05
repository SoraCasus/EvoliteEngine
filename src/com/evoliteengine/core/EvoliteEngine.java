package com.evoliteengine.core;

public class EvoliteEngine implements Runnable {

	private final Thread thread;

	public EvoliteEngine (String title, int width, int height) {
		this.thread = new Thread(this, "EVOLITE_ENGINE_THREAD");
	}

	@Override
	public void run () {

	}
}
