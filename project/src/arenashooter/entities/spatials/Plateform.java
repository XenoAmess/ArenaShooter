package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.Collider;

public class Plateform extends Spatial {
	
	private Vec2f extent;
	

	public Plateform(Vec2f position, Vec2f extent) {
		super(position);
		Collider coll = new Collider(this.position, extent);
		coll.extent = extent;
		this.extent = extent;
		coll.attachToParent(this, "collider");
		
		//TODO: passer sprite & rgb
		Sprite spr = new Sprite(position);
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		spr.attachToParent(this, "sprite");
		
		StaticBody staticBody = new StaticBody(new Rectangle(extent), position, rotation);
		StaticBodyContainer staticBodyC = new StaticBodyContainer(position, staticBody);
		staticBodyC.attachToParent(this, "static body");
	}
	
	public float getExtentX() {
		return extent.x;
	}
	
	public float getExtentY() {
		return extent.y;
	}
	
	@Override
	public void step(double d) {
		((Spatial)children.get("sprite")).position = position;
		((Spatial)children.get("collider")).position = position;
		super.step(d);
	}
}