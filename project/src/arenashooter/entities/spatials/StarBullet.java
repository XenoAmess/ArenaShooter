package arenashooter.entities.spatials;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.game.Main;

public class StarBullet extends Projectile {
	
	//private Vec2f fallSpeed = new Vec2f(0, 2);

	public StarBullet(Vec2fi position, Vec2fi vel, float damage) {
		super(new RigidBody(new ShapeDisk(.25), position, vel.angle(), CollisionFlags.PROJ, (float)0.5, 138));
		
		getBody().setBullet(true);
		getBody().setIsSensor(true);
		
		this.vel = new Vec2f(vel);

		this.damage = damage;

		Sprite sprite = new Sprite(new Vec2f(), "data/sprites/star.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
		sprite.getTexture().setFilter(false);
		sprite.attachToParent(this, "bul_Sprite");
	}

	@Override
	public void impact(Spatial other) {
		if(other == shooter) return; //Ignore instigator
		
		if(other instanceof Character)
		((Character)other).stun(damage);
		
		if(Math.random() > 0.5)
			Main.getAudioManager().playSound2D("data/sound/mbggnea1.ogg", AudioChannel.SFX, .25f, (float)( 0.90 + Math.random()*0.2), getWorldPos());
		else if(Math.random() > 0.5)//Oui, les sons ne sont pas equiprobables.
			Main.getAudioManager().playSound2D("data/sound/mbggnea2.ogg", AudioChannel.SFX, .25f, (float)( 0.90 + Math.random()*0.2), getWorldPos());
		else
			Main.getAudioManager().playSound2D("data/sound/mbggnea3.ogg", AudioChannel.SFX, .25f, (float)( 0.90 + Math.random()*0.2), getWorldPos());
		
		detach();
	}

	@Override
	public void step(double d) {
		if(getBody().getBody() != null)
			getBody().getBody().setGravityScale(6);
		
		getBody().setLinearVelocity(vel);

		super.step(d);
	}
}
