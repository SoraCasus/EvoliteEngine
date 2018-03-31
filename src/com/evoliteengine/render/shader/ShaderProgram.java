package com.evoliteengine.render.shader;


import com.evoliteengine.core.IDisposable;
import com.evoliteengine.render.shader.uniform.Uniform;
import com.evoliteengine.util.EEFile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram implements IDisposable {

	private int programID;

	public ShaderProgram (EEFile shader, String... inVars) {
		ShaderIDs ids = loadShader(shader);
		this.programID = GL20.glCreateProgram();

		if (ids.vertID != -1)
			GL20.glAttachShader(programID, ids.vertID);
		if (ids.fragID != -1)
			GL20.glAttachShader(programID, ids.fragID);
		if (ids.geomID != -1)
			GL20.glAttachShader(programID, ids.geomID);
		if (ids.tessCtrlID != -1)
			GL20.glAttachShader(programID, ids.tessCtrlID);
		if (ids.tessEvalID != -1)
			GL20.glAttachShader(programID, ids.tessEvalID);

		bindAttrributes(inVars);

		GL20.glLinkProgram(programID);
		checkError(programID, GL20.GL_LINK_STATUS, true);

		if (ids.vertID != -1) {
			GL20.glDetachShader(programID, ids.vertID);
			GL20.glDeleteShader(ids.vertID);
		}

		if (ids.fragID != -1) {
			GL20.glDetachShader(programID, ids.fragID);
			GL20.glDeleteShader(ids.fragID);
		}

		if (ids.geomID != -1) {
			GL20.glDetachShader(programID, ids.geomID);
			GL20.glDeleteShader(ids.geomID);
		}

		if (ids.tessCtrlID != -1) {
			GL20.glDetachShader(programID, ids.tessCtrlID);
			GL20.glDeleteShader(ids.tessCtrlID);
		}

		if (ids.tessEvalID != -1) {
			GL20.glDetachShader(programID, ids.tessEvalID);
			GL20.glDeleteShader(ids.tessEvalID);
		}
	}

	protected void storeUniormLocation (Uniform... uniforms) {
		for (Uniform u : uniforms)
			u.storeUniformLocation(programID);
		GL20.glValidateProgram(programID);
		checkError(programID, GL20.GL_VALIDATE_STATUS, true);
	}

	private void bindAttrributes (String[] attribs) {
		for (int i = 0; i < attribs.length; i++)
			GL20.glBindAttribLocation(programID, i, attribs[i]);
	}

	private ShaderIDs loadShader (EEFile shader) {
		ShaderIDs res = new ShaderIDs();

		Map<String, StringBuilder> sources = parseShader(shader);

		res.vertID = compileShader(sources.get("VERT"), GL20.GL_VERTEX_SHADER);
		res.fragID = compileShader(sources.get("FRAG"), GL20.GL_FRAGMENT_SHADER);
		res.geomID = compileShader(sources.get("GEOM"), GL32.GL_GEOMETRY_SHADER);
		res.tessEvalID = compileShader(sources.get("TESS_EVAL"), GL40.GL_TESS_EVALUATION_SHADER);
		res.tessCtrlID = compileShader(sources.get("TESS_CTRL"), GL40.GL_TESS_CONTROL_SHADER);

		return res;
	}

	private Map<String, StringBuilder> parseShader (EEFile shader) {
		Map<String, StringBuilder> res = new HashMap<>();
		res.put("VERT", new StringBuilder());
		res.put("FRAG", new StringBuilder());
		res.put("GEOM", new StringBuilder());
		res.put("TESS_EVAL", new StringBuilder());
		res.put("TESS_CTRL", new StringBuilder());
		StringBuilder current = null;

		try (BufferedReader reader = shader.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#shader")) {

					if (line.startsWith("#shader vertex")) {
						current = res.get("VERT");
					} else if (line.startsWith("#shader fragment")) {
						current = res.get("FRAG");
					} else if (line.startsWith("#shader geometry")) {
						current = res.get("GEOM");
					} else if (line.startsWith("#shader tess control")) {
						current = res.get("TESS_CTRL");
					} else if (line.startsWith("#shader tess evaluation")) {
						current = res.get("TESS_EVAL");
					} else {
						current = null;
					}
				} else if (line.startsWith("#include")) {
					if (current == null)
						throw new IllegalStateException("Shader is not formatted properly! " + shader.toString());

					// Todo(Sora): Include files for shaders

				} else {
					if (current == null)
						throw new IllegalStateException("Shader is not formatted properly! " + shader.toString());

					current.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private int compileShader (StringBuilder source, int type) {
		int id = -1;
		if (!source.toString().isEmpty()) {
			id = GL20.glCreateShader(type);
			GL20.glShaderSource(id, source);
			GL20.glCompileShader(id);
			if (checkError(id, GL20.GL_COMPILE_STATUS, false)) {
				System.err.println("Shader: " + getShaderTypeString(type) + ", " + source);
			}
		}
		return id;
	}

	private String getShaderTypeString (int type) {
		String res;
		// @formatter:off
		switch(type) {
			case GL20.GL_VERTEX_SHADER : {
				res = "VERTEX";
			} break;

			case GL20.GL_FRAGMENT_SHADER : {
				res = "FRAGMENT";
			} break;

			case GL32.GL_GEOMETRY_SHADER : {
				res = "GEOMETRY";
			} break;

			case GL40.GL_TESS_CONTROL_SHADER : {
				res = "TESSELATION CONTROL";
			} break;

			case GL40.GL_TESS_EVALUATION_SHADER : {
				res = "TESSELATION EVALUATION";
			} break;

			default : {
				res = "UNKNOWN";
			} break;
		}
		// @formatter:on
		return res;
	}

	private boolean checkError (int id, int flag, boolean isProgram) {
		if (isProgram) {
			if (GL20.glGetProgrami(id, flag) != GL11.GL_TRUE) {
				// An error was found
				int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
				System.err.println(GL20.glGetProgramInfoLog(id, length));
				return true;
			}
		} else {
			if (GL20.glGetShaderi(id, flag) != GL11.GL_TRUE) {
				// An error was found
				int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
				System.err.println(GL20.glGetShaderInfoLog(id, length));
				return true;
			}
		}
		return false;
	}

	public void start () {
		GL20.glUseProgram(programID);
	}

	public void stop () {
		GL20.glUseProgram(0);
	}

	@Override
	public void delete () {
		GL20.glDeleteProgram(programID);
	}

	private class ShaderIDs {
		private int vertID;
		private int fragID;
		private int geomID;
		private int tessEvalID;
		private int tessCtrlID;
	}

}
