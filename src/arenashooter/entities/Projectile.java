package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Projectile extends Spatial{
	
	//TODO Link projectiles to Character.

	boolean disp = false;
	Vec2f vel = new Vec2f();
	Collider collider;
	float damage;//Un projectile n'a pas forcément de damage.

	public Projectile() {
		super();
	}
	
	public Projectile(Vec2f position) {
		super(position);
	}
	
}
