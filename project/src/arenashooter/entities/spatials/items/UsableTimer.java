package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public class UsableTimer extends Item {

	/** Time in between attacks */
	protected Timer timerCooldown = null;
	protected double fireRate = 0;
	protected int duration = 0;
	protected String animPath = "";
	protected double warmup = 0;
	protected String soundWarmup = "";
	protected String soundFire = "";

	public UsableTimer(Vec2f localPosition, String name, double weight, String pathSprite, Vec2f handPosL,
			Vec2f handPosR, Vec2f size, String soundPickup, double fireRate, int duration, String animPath,
			double warmup, String soundWarmup, String soundFire) {
		super(localPosition, name, weight, pathSprite, handPosL, handPosR, size, soundPickup);
		timerCooldown = new Timer(fireRate);
		timerCooldown.attachToParent(this, timerCooldown.genName());
		this.fireRate = fireRate;
		this.duration = duration;
		this.animPath = animPath;
		this.warmup = warmup;
		this.soundWarmup = soundWarmup;
		this.soundFire = soundFire;
	}

	public void attackStart() {
		timerCooldown.setIncreasing(true);
		timerCooldown.setProcessing(true);
	}

	public void attackStop() {
		timerCooldown.setIncreasing(false);
	}

	public void step(double d) {
		if (timerCooldown.isOver()) {
			timerCooldown.restart();

		}
		Vec2f targetOffSet = Vec2f.rotate(new Vec2f(50, 0), getWorldRot());
		localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
		localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
		if (isEquipped()) {
			localRotation = Utils.lerpAngle(localRotation, ((Character) getParent()).aimInput, Math.min(1, d * 17));
			getSprite().localRotation = getWorldRot();
		}

		super.step(d);
	}

	@Override
	public UsableTimer clone() {
		UsableTimer clone = new UsableTimer(localPosition, animPath, duration, animPath, handPosL, handPosR, extent,
				animPath, fireRate, duration, animPath, warmup, soundWarmup, soundFire);
		return clone;
	}
}
