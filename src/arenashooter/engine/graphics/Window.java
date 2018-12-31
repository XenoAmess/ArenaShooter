package arenashooter.engine.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import arenashooter.engine.Input;
import arenashooter.engine.math.Mat4f;

/**
 * Game window
 */

public final class Window {
	private static final int WIDTH_MIN = 640, HEIGHT_MIN = 480;
	
	private static long window;
	private static GLFWVidMode vidmode;
	private static int width, height;
	private static float ratio;
	
	private static float fov = 90;
	private static final float CLIP_NEAR = 10, CLIP_FAR = 10000;
	
	//This class cannot be instantiated
	private Window() {}
	
	public static void init(int windowWidth, int windowHeight, String windowTtitle) {
		System.out.println("Render - Initializing");
		
		GLFWErrorCallback errorfun = GLFWErrorCallback.createPrint();
		glfwSetErrorCallback(errorfun);
		
		if(!glfwInit()) {
			System.err.println("Render - Can't initialize GLFW !");
			System.exit(1);
		}
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//On s'assure que la fenetre respecte les tailles minimales et maximales
		width = Math.max(WIDTH_MIN, Math.min(windowWidth, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(windowHeight, vidmode.height()));
		ratio = (float)width/(float)height;
		
		glfwDefaultWindowHints();
		
		//Interdire le redimensionnement manuel
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		//Masquer la fenetre jusqu'a ce qu'elle soit entierement creee
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		//Set openGL version to 3.2
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		window = glfwCreateWindow(width, height, windowTtitle, NULL, NULL);

		if (window == NULL) {
			System.err.println("Render - Can't create window !");
			System.exit(1);
		}
		
		setIcon( new String[] {"data/icon_32.png", "data/icon_64.png", "data/icon_128.png"} );
		
		//Center window
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2 );
		
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
		glEnable(GL_DEPTH_TEST);
	    glDepthFunc(GL_LEQUAL);
		
		//Set the clear color to black
		glClearColor(0, 0, 0, 0);
		
		//Show the window
		glfwShowWindow(window);
		
		//Link keyboard input to the window
		Input.setWindow(window);

		//Create projection matrix
		createProjectionMatrix();
		
		//TODO: Temp test stuff
		shaderSky = new Shader("data/shaders/test_sky");
		quad = Model.loadQuad();
	}
	
	/**
	 * @return User tries to close the window
	 */
	public static boolean requestClose() {
		return glfwWindowShouldClose(window);
	}
	
	//TODO: Temp variables
	static Model quad;
	static Shader shaderSky;
	public static Mat4f proj;
	
	/**
	 * Begin a frame, this will update Input and prepare the window for rendering
	 */
	public static void beginFrame() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glfwPollEvents();
		Input.update();
		
		drawSky(); //TODO: Remove this
	}
	
	/**
	 * End a frame, this will swap framebuffers
	 */
	public static void endFrame() {
		glfwSwapBuffers(window);
	}
	
	private static void drawSky() { //TODO: Remove this temp function
		shaderSky.bind();
		
		quad.bindToShader(shaderSky);
		
		quad.bind();
		quad.draw();
		
		Model.unbind();
		Shader.unbind();
	}
	
	/**
	 * Destroy the fenetre
	 */
	public static void destroy() {
		System.out.println("Render - Stopping");
		
		glfwDestroyWindow(window);
	}
	
	/**
	 * Change the window size
	 * @param newWidth
	 * @param newHeight
	 */
	public static void resize(int newWidth, int newHeight) {
		width = Math.max(WIDTH_MIN, Math.min(newWidth, vidmode.width()));
		height = Math.max(HEIGHT_MIN, Math.min(newHeight, vidmode.height()));
		
		ratio = (float)width/(float)height;
		
		glfwSetWindowSize(window, width, height);
		glViewport(0, 0, width, height);

		//Recreate projection matrix
		createProjectionMatrix();
	}
	
	/**
	 * Get screen aspect ratio (width/height)
	 */
	public static float getRatio() {
		return ratio;
	}
	
	/**
	 * Set the camera's field of view
	 * @param verticalFOV new FOV
	 */
	public static void setFOV(float verticalFOV) {
		fov = verticalFOV;
		createProjectionMatrix();
	}
	
	/**
	 * Create the projection matrix based on window size
	 */
	private static void createProjectionMatrix() {
//		float sizeY = 800;
//		float sizeX = sizeY*ratio;
//		proj = Mat4f.ortho(0.1f, 500, -sizeX/2, sizeY/2, sizeX/2, -sizeY/2);
		proj = Mat4f.perspective(CLIP_NEAR, CLIP_FAR, fov, ratio);
	}
	
	/**
	 * Set the window title
	 * @param title new title
	 */
	public static void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	/**
	 * Enable or disable vertical synchronization
	 * @param enable new vSync state
	 */
	public static void setVsync(boolean enable) {
		glfwSwapInterval( enable ? 1 : 0 );
	}
	
	private static void setIcon(String[] paths) {
		Image image[] = new Image[paths.length];
		
		for( int i=0; i<paths.length; i++ )
			image[i] = Image.loadImage(paths[i]);
		
		GLFWImage.Buffer images = GLFWImage.malloc(paths.length);
		try {
			for( int i=0; i<paths.length; i++ ) {
				images
					.position(i)
					.width(image[i].width)
					.height(image[i].height)
					.pixels(image[i].buffer);
			}
			
			glfwSetWindowIcon(window, images);
				
		} catch(Exception e) {
			System.err.println("Render - Error loading window icons !");
			e.printStackTrace();
		}
	}
}
