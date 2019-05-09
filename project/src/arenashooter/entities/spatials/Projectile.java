package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;

public class Projectile extends Spatial {

	public Character shooter;

	boolean disp = false;
	Vec2f vel = new Vec2f();
	Collider collider;
	float damage; //Un projectile n'a pas forcément de damage.

	public Projectile() {
		super();
	}

	public boolean isShooter(Character dude) {
			return dude == shooter;
	}

	public Projectile(Vec2f position) {
		super(position);
	}

}
