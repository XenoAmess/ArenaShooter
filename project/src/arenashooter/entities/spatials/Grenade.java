package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.Timer;

public class Grenade extends Projectile {

	private Timer grenadeTimer;
	
	private boolean launched = false;
	private AnimationData redBlinks = AnimationData.loadAnim("data/animations/BlinkRed_1.xml");
	private Animation blinking = null;

	public Grenade(Vec2fi position, Vec2fi vel, float damage) {
		super(new RigidBody(new ShapeBox(new Vec2f(.25, .15)), position, vel.angle(), CollisionFlags.PROJ, 4, 1));
		
		getBody().setBullet(true);
		
		this.vel = new Vec2f(vel);

		this.damage = damage;
//		localRotation = vel.angle();

		Sprite sprite = new Sprite(new Vec2f(), "data/sprites/grenade_01.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.06, sprite.getTexture().getHeight()*.06);
		sprite.getTexture().setFilter(false);
		sprite.attachToParent(this, "bul_Sprite");

		grenadeTimer = new Timer(2);
		grenadeTimer.setIncreasing(true);
		grenadeTimer.setProcessing(true);
		grenadeTimer.attachToParent(this, "timer");
	}

	@Override
	public void impact(Spatial other) { } //Don't do anything special on impact, just bounce around

	@Override
	public void step(double d) {
		if(blinking == null && grenadeTimer.getMax()- grenadeTimer.getValue() < 1 ) {
			blinking = new Animation(redBlinks);
			blinking.play();
		}
		if(blinking != null) {
			blinking.step(d);
			((Sprite)getChild("bul_Sprite")).material.setParamVec4f("baseColorMod", new Vec4f(blinking.getTrackVec3f("BlinkColor"), 1f));
		}
			
		if(grenadeTimer.isOver()) {
			Explosion explosion = new Explosion(getWorldPos(), new DamageInfo(50, DamageType.EXPLOSION, new Vec2f(), 20, shooter), 20);
			explosion.attachToParent(getArena(), explosion.genName());
			detach();
		}
		
		if(!launched && getBody().getBody() != null) {
			launched = true;
			getBody().setLinearVelocity(vel);
		}

		super.step(d);
	}
}
