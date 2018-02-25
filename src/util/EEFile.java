package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EEFile {
	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;

	public EEFile(String path) {
		this.path = FILE_SEPARATOR + path;
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public EEFile(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public EEFile(EEFile file, String subFile) {
		this.path = file.path + FILE_SEPARATOR + subFile;
		this.name = subFile;
	}

	public EEFile(EEFile file, String... subFiles) {
		this.path = file.path;
		for (String part : subFiles) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString(){
		return getPath();
	}

	public InputStream getInputStream() {
		return this.getClass().getResourceAsStream(path);
	}

	public BufferedReader getReader() {
		InputStreamReader isr = new InputStreamReader(getInputStream());
		return new BufferedReader(isr);
	}

	public String getName() {
		return name;
	}
	

}
