package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import arenashooter.engine.Input;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2d;
import arenashooter.engine.math.Vec3f;

/**
 * Game window
 */

public class Window {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	private long window;
	private GLFWVidMode vidmode;
	private int width, height;
	
	public Window(int width, int height, String title) {
		if(!glfwInit()) System.err.println("Can't initialize GLFW !");
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//On s'assure que la fenetre respecte les tailles minimales et maximales
		this.width = Math.max(WIDTH_MIN, Math.min(width, vidmode.width()));
		this.height = Math.max(HEIGHT_MIN, Math.min(height, vidmode.height()));

		glfwDefaultWindowHints();
		
		//Interdire le redimensionnement manuel
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		//Masquer la fenetre jusqu'a ce qu'elle soit entierement creee
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		//OpenGL 3.2
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		window = glfwCreateWindow(this.width, this.height, title, NULL, NULL);

		if (window == NULL) System.err.println("Can't create window !");
		
		//Center window
		glfwSetWindowPos(window, (vidmode.width()-this.width)/2, (vidmode.height()-this.height)/2 );
		
		//Definir le contexte de la fenetre
		glfwMakeContextCurrent(window);
		
		//Lier la fenetre a OpenGL
		GL.createCapabilities();
		
		//Enable textures
		glEnable(GL_TEXTURE_2D);
		
		//Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//Enable depth sorting
//		glEnable(GL_DEPTH_TEST);
//	    glDepthFunc(GL_LEQUAL);
		
		//Set the clear color to black
		glClearColor(0, 0, 0, 0);
		
		//Show the window
		glfwShowWindow(window);
		
		//Link keyboard input to the window
		Input.setWindow(window);
		
		//TODO: Temp test stuff
		proj = Mat4f.perspective(-1, 10, 90, (float)width/(float)height);
		createVBOs();
		tex = new Texture("data/test.png"); //Texture de test
		shader1 = new Shader("data/shaders/shader_test_120");
		shader2 = new Shader("data/shaders/shader_test_2_120");
		square = Model.loadSquare();
	}
	
	/**
	 * @return Si l'utilisateur demande la fermeture de la fenetre
	 */
	public boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	//Boule magique
	Vec2d pos = new Vec2d();
	Vec2d vel = new Vec2d();
	double size = 200;
	Texture tex;
	Shader shader1, shader2;
	Mat4f proj;
	
	public void update( double delta ) {
		//Physique et controles de la boule magique
		vel.x = Utils.lerpD(vel.x, Input.getAxis("moveX")*500, delta*5);
		
		if( Input.actionPressed("jump") )
			if( pos.y == 450 )
				vel.y = -800;
		
		if(pos.y < 450)
			vel.y += 9.807*200*delta;

		pos.add(Vec2d.multiply(vel, delta));
		
		pos.y = Math.min(450, pos.y);
		
		//Projection orthographique
		glLoadIdentity();
		glOrtho(0, width, height, 0, 10, -10);
		
		//Sky //TODO: Remove deprecated sky rendering code
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glVertexPointer(3, GL_FLOAT, 0, 0l);

		glBindBuffer(GL_ARRAY_BUFFER, vboCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glColor3d(.8, .8, 1);
		
		glPushMatrix();
		glScaled(width, height, 1);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glPopMatrix();
		
		glColor3f(1, 1, 1);
		
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		///End of sky
		
		//Boule magique
		drawBouleMagique2();
		
		glfwSwapBuffers(window);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwPollEvents();
		Input.update();
	}
	
	Model square;
	void drawBouleMagique2() { //TODO: Use this
		//TODO: Fix mvp uniforms and remove this
		glPushMatrix();
		glTranslated(pos.x, pos.y, 0);
		glScaled(size, size, 1);
		
		//Create matrices
		Vec3f pos3f = new Vec3f( (float)pos.x+.2f, (float)pos.y, 0 );
		Vec3f rot = new Vec3f(0, 0, 0);
		Vec3f scale = new Vec3f( (float)size, (float)size, (float)size );
		Mat4f model = Mat4f.transform(pos3f, rot, scale);
		shader1.setUniformM4("model", model);
		shader1.setUniformM4("view", Mat4f.identity());
		shader1.setUniformM4("projection", proj);
		
		shader2.bind();
		shader2.setUniformM4("model", model);
		shader2.setUniformM4("projection", proj);
		
		square.bindToShader(shader2);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader1.setUniformI("baseColor", GL_TEXTURE0);
		
		//Color change
		shader1.setUniformF("colorMod", (float)(Math.sin(System.currentTimeMillis()/100d)+1d)/2f);
		
		square.bind();
		square.draw();
		
		Model.unbind();
		Shader.unbind();
		Texture.unbind();
		
		//TODO: remove this
		glPopMatrix();
	}
	
	/**
	 * Destroy the fenetre
	 */
	public void destroy() {
		glfwDestroyWindow(window);
	}
	
	/**
	 * Change the window size
	 * @param newWidth
	 * @param newHeight
	 */
	public void resize(int newWidth, int newHeight) {
		width = Math.max(WIDTH_MIN, Math.min(newWidth, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(newHeight, vidmode.height()));
		
		glfwSetWindowSize(window, width, height);
		glViewport(0, 0, width, height);
		proj = Mat4f.perspective(-1, 10, 90, (float)width/(float)height);
	}
	
	/**
	 * Set the window title
	 * @param title new title
	 */
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	/**
	 * Enable or disable vertical synchronization
	 * @param enable new vSync state
	 */
	public void setVsync(boolean enable) {
		glfwSwapInterval( enable ? 1 : 0 );
	}
	
	int vboVertex, vboCoords;
	private void createVBOs() { //TODO: replace this with an interleaved vbo
		int vertices = 6;

		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices * 3);
		vertexData.put(new float[] { 1f, 0f, 0f });
		vertexData.put(new float[] { 0f, 0f, 0f });
		vertexData.put(new float[] { 0f, 1f, 0f });
		
		vertexData.put(new float[] { 0f, 1f, 0f });
		vertexData.put(new float[] { 1f, 1f, 0f });
		vertexData.put(new float[] { 1f, 0f, 0f });
		vertexData.flip();

		FloatBuffer uvData = BufferUtils.createFloatBuffer(vertices * 2);
		uvData.put(new float[] { 1f, 0f });
		uvData.put(new float[] { 0f, 0f });
		uvData.put(new float[] { 0f, 1f });
		
		uvData.put(new float[] { 0f, 1f });
		uvData.put(new float[] { 1f, 1f });
		uvData.put(new float[] { 1f, 0f });
		uvData.flip();
		
		vboVertex = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboCoords = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboCoords);
		glBufferData(GL_ARRAY_BUFFER, uvData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
