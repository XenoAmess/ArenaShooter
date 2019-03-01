package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import arenashooter.engine.FileUtils;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Shader {
	private static HashMap<String, Shader> cache = new HashMap<>();
	
	private int vertex, fragment, program;
	/** Does this shader require transparency? */
	public boolean transparent = false;
//	private static int boundShader = 0;
	
	private Shader(int vertex, int fragment, int program) {
		this.vertex = vertex;
		this.fragment = fragment;
		this.program = program;
	}
	
	/**
	 * Tries to load a shader from cache or load a new one. 
	 * Create a shader from vertex and fragment shader files sharing the same path and name.
	 * Appends ".vert" and ".frag" to get the complete file paths
	 * @param path resource path to the files, excluding file extensions
	 * @return 
	 */
	public static Shader loadShader(String path) {
		Shader cached = cache.get(path);
		if(cached != null) return cached;
		
		Shader shader = loadShader(path+".vert", path+".frag");
		cache.put(path, shader);
		return shader;
	}
	
	/**
	 * Create a shader from vertex and fragment shader files
	 * @param vertexPath resource path to the vertex shader
	 * @param fragmentPath resource path to the fragment shader
	 * @return 
	 */
	private static Shader loadShader(String vertexPath, String fragmentPath) {
		int vertex = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertex, FileUtils.resToString(vertexPath));
		glCompileShader(vertex);
		if( glGetShaderi(vertex, GL_COMPILE_STATUS) != GL_TRUE ) {
			System.err.println( "Can't compile vertex shader: "+vertexPath );
			System.err.println( glGetShaderInfoLog(vertex) );
		}
		
		int fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, FileUtils.resToString(fragmentPath));
		glCompileShader(fragment);
		if( glGetShaderi(fragment, GL_COMPILE_STATUS) != GL_TRUE ) {
			System.err.println( "Can't compile fragment shader: "+fragmentPath );
			System.err.println( glGetShaderInfoLog(fragment) );
		}
		
		int program = glCreateProgram();
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		
		glLinkProgram(program);
		if( glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE ) {
			System.err.println( "Can't link shaders: "+vertexPath+", "+fragmentPath );
			System.err.println( glGetProgramInfoLog(program) );
		}
		glValidateProgram(program);
		if( glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE ) {
			System.err.println( "Can't validate shaders: "+vertexPath+", "+fragmentPath );
			System.err.println( glGetProgramInfoLog(program) );
		}
		
		return new Shader(vertex, fragment, program);
	}
	
	public int getAttribLocation(String name) { return glGetAttribLocation(program, name); }
	
	/**
	 * Set a Matrix4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformM4(String name, Mat4f value) {
		int location = glGetUniformLocation(program, name);
		glUniformMatrix4fv(location, false, value.toArray());
	}
	
	/**
	 * Set a Vec3f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV3(String name, Vec3f value) {
		int location = glGetUniformLocation(program, name);
		glUniform3fv(location, value.toArray());
	}
	
	/**
	 * Set a float[3] uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV3(String name, float[] value) {
		int location = glGetUniformLocation(program, name);
		glUniform3fv(location, value);
	}
	
	/**
	 * Set a float[4] uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV4(String name, float[] value) {
		int location = glGetUniformLocation(program, name);
		glUniform4fv(location, value);
	}
	
	/**
	 * Set a Vec4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV4(String name, Vec4f value) {
		int location = glGetUniformLocation(program, name);
		glUniform4fv(location, new float[] {value.x, value.y, value.z, value.w});
	}
	
	/**
	 * Set a float uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformF(String name, float value) {
		int location = glGetUniformLocation(program, name);
		glUniform1f(location, value);
	}
	
	/**
	 * Set an int uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformI(String name, int value) {
		int location = glGetUniformLocation(program, name);
		glUniform1i(location, value);
	}
	
	/**
	 * Use this shader
	 */
	public void bind() {
//		if( program == boundShader ) return;
		glUseProgram(program);
//		boundShader = program;
	}
	
	/**
	 * Stop current shader
	 */
	public static void unbind() {
		glUseProgram(0);
//		boundShader = 0;
	}
	
	//
	//Memory Management
	//
	
	/**
	 * Remove unused shaders from memory
	 */
	public static void cleanShaders() {
		//TODO
	}
}
