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

	protected void bindAttribute (String name, int index, boolean done) {
		GL20.glBindAttribLocation(programID, index, name);
		if(done) {
			GL20.glValidateProgram(programID);
			checkError(programID, GL20.GL_VALIDATE_STATUS, true);
		}
	}

	private ShaderIDs loadShader (EEFile shader) {
		ShaderIDs res = new ShaderIDs();

		StringBuilder vertSrc = new StringBuilder();
		StringBuilder fragSrc = new StringBuilder();
		StringBuilder geomSrc = new StringBuilder();
		StringBuilder tessEvalSrc = new StringBuilder();
		StringBuilder tessCtrlSrc = new StringBuilder();
		StringBuilder current = null;


		try (BufferedReader reader = shader.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#shader")) {

					if (line.startsWith("#shader vertex")) {
						current = vertSrc;
					} else if (line.startsWith("#shader fragment")) {
						current = fragSrc;
					} else if (line.startsWith("#shader geometry")) {
						current = geomSrc;
					} else if (line.startsWith("#shader tess control")) {
						current = tessCtrlSrc;
					} else if (line.startsWith("#shader tess evaluation")) {
						current = tessEvalSrc;
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

		if (!vertSrc.toString().isEmpty()) {
			int id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			GL20.glShaderSource(id, vertSrc);
			GL20.glCompileShader(id);
			if(checkError(id, GL20.GL_COMPILE_STATUS, false)){
				System.err.println("Shader: VERTEX, " + shader);
			}
			res.vertID = id;
		}

		if (!fragSrc.toString().isEmpty()) {
			int id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
			GL20.glShaderSource(id, fragSrc);
			GL20.glCompileShader(id);
			if(checkError(id, GL20.GL_COMPILE_STATUS, false)){
				System.err.println("Shader: FRAGMENT, " + shader);
			}
			res.fragID = id;
		}

		if (!geomSrc.toString().isEmpty()) {
			int id = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
			GL20.glShaderSource(id, geomSrc);
			GL20.glCompileShader(id);
			if(checkError(id, GL20.GL_COMPILE_STATUS, false)){
				System.err.println("Shader: GEOMETRY, " + shader);
			}
			res.geomID = id;
		}

		if (!tessEvalSrc.toString().isEmpty()) {
			int id = GL20.glCreateShader(GL40.GL_TESS_EVALUATION_SHADER);
			GL20.glShaderSource(id, tessEvalSrc);
			GL20.glCompileShader(id);
			if(checkError(id, GL20.GL_COMPILE_STATUS, false)){
				System.err.println("Shader: TESSELATION EVALUATION, " + shader);
			}
			res.tessEvalID = id;
		}

		if (!tessCtrlSrc.toString().isEmpty()) {
			int id = GL20.glCreateShader(GL40.GL_TESS_CONTROL_SHADER);
			GL20.glShaderSource(id, tessCtrlSrc);
			GL20.glCompileShader(id);
			if(checkError(id, GL20.GL_COMPILE_STATUS, false)){
				System.err.println("Shader: TESSELATION CONTROL, " + shader);
			}
			res.tessCtrlID = id;
		}

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
		int vertID = -1;
		int fragID = -1;
		int geomID = -1;
		int tessEvalID = -1;
		int tessCtrlID = -1;
	}

}
