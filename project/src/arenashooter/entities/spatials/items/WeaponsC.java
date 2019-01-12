package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Bullet;
import arenashooter.game.Game;
import arenashooter.entities.spatials.Character;

public class WeaponsC extends Item {
	
	private double dispersion = 0.5;//la non-précision.
	private Timer fire = new Timer(0.25);
	Collider coll;

	public WeaponsC(Vec2f position, ItemSprite itemSprite) {
		super(position, itemSprite);
		fire.attachToParent(this, "attack timer");
		tag = "Arme";
		coll = new Collider(position, new Vec2f(40, 40));
	}

	public void fire(boolean lookRight) { // Visée uniquement droite et gauche pour l'instant. TODO :
		if (fire.isOver()) {
			float pX = 0;
			float vX = 0;
			if (parent instanceof Character) {
				if (lookRight) {
					pX = position.x + 70;
					vX = 1500+((Character)parent).vel.x;
				} else {
					pX = position.x - 70;
					vX = -1500;
				}
			}
			fire.restart();
			
			Vec2f angle = Vec2f.rotate(new Vec2f(vX, 0), dispersion);
			angle.x += ((Character)parent).vel.x;
			angle.y += ((Character)parent).vel.y;
			
			Bullet bul = new Bullet(new Vec2f(pX, position.y), angle);
			bul.attachToParent(Game.game.map, ("bullet" + bul.genName()));
		}
	}

	public void step(double d) {
		super.step(d);
	}
}
