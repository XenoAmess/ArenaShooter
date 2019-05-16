package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.CircleShape;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicShape;

public class ShapeDisk extends PhysicShape {
	private double radius;
	
	public ShapeDisk(double radius) {
		b2Shape = new CircleShape();
		b2Shape.setRadius((float) radius);
		this.radius = radius;
	}
	
	public double getRadius() { return radius; }
	
	public void setRadius(double newRadius) {
		this.radius = newRadius;
		b2Shape.setRadius((float) newRadius);
	}
	
	private static final Model disk = Model.loadDisk(16);
	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	@Override
	public void debugDraw(Vec2f pos, double rot) {
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(pos, rot, new Vec2f( radius*2 ));
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		disk.bindToShader(shader);

		disk.bind();
		disk.draw(true);
	}
}
