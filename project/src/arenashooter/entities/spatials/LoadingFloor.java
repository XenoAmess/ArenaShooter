package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;

public class LoadingFloor extends Spatial {
	private static final Texture[] tex;
//	private static final Shader shader;
	private static final Material material;
	private static final Model model;
	static private final Vec2f size = new Vec2f(128, 256);
	
	private int currentTex;
	private double timer = 0;
	
	static {
//		shader = Shader.loadShader("data/shaders/sprite_simple");
		material = new Material("data/shaders/sprite_simple");
		model = Model.loadQuad();
		tex = new Texture[] {
				Texture.loadTexture("data/sprites/loading_floor/floor_01.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_02.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_03.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_04.png"),
				Texture.loadTexture("data/sprites/loading_floor/floor_05.png")
		};
	}
	
	public LoadingFloor(Vec2f position) {
		super(position);
		currentTex = (int)Math.floor( Math.random()*(tex.length-1) );
	}
	
	@Override
	public void step(double d) {
		timer+=d;

		if(timer >= .2) {
			timer = 0;
			
			currentTex++;
			if(currentTex >= tex.length) {
				currentTex = 0;
				size.x = -size.x;
			}
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		
		Profiler.startTimer(Profiler.SPRITES);
		
		material.setParamTex("baseColor", tex[currentTex]);
		material.model = Mat4f.transform(parentPosition, rotation, size);
		material.view = Window.getView();
		material.proj = Window.proj;
		material.bind(model);
		
		model.bind();
		model.draw();
		
		Profiler.endTimer(Profiler.SPRITES);
	}
}
