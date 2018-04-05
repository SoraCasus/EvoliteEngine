package com.evoliteengine.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private final WindowOpts opts;
	private long windowID;
	private boolean resized;

	public Window (WindowOpts opts) {
		this.opts = opts;
		this.resized = false;
		this.windowID = NULL;
	}

	public void init () {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, opts.fullscreen ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, opts.samples);

		this.windowID = GLFW.glfwCreateWindow(opts.width, opts.height, opts.title, NULL, NULL);
		if (this.windowID == NULL)
			throw new RuntimeException("Failed to create GLFW window!");

		GLFW.glfwSetFramebufferSizeCallback(this.windowID, (window, width, height) -> {
			this.opts.width = width;
			this.opts.height = height;
			this.resized = true;
		});

		//Todo(Sora): Figure out how input is going to work >.>
		GLFW.glfwSetKeyCallback(this.windowID, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
				GLFW.glfwSetWindowShouldClose(window, true);
		});

		if (!opts.fullscreen) {
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			assert (vidMode != null);
			GLFW.glfwSetWindowPos(windowID,
					(vidMode.width() - opts.width) / 2,
					(vidMode.height() - opts.height) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(windowID);

		if (opts.vSync)
			GLFW.glfwSwapInterval(1);

		GL.createCapabilities();

		GL11.glClearColor(0F, 0F, 0F, 0F);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
	}

	public void update () {
		GLFW.glfwSwapBuffers(windowID);
		GLFW.glfwPollEvents();
	}

	public class WindowOpts {
		public String title;
		public int width;
		public int height;
		public boolean vSync;
		public boolean fullscreen;
		public int samples;
	}

}
