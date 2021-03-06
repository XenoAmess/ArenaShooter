package arenashooter.game;

import arenashooter.engine.ContentManager;
import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.entities.spatials.Character;
import arenashooter.game.gameStates.Game;

public abstract class Controller {
	/** This controller's character information */
	public CharacterInfo info;

	/** Currently possessed character */
	private Character character;

	public int playerNumber;

	public int team = 1;
	boolean deadChar = false;

	/**
	 * Stats for Score
	 */
	public int roundsWon = 0;
	public int deaths = 0;
	public int kills = 0;
	public int flagCatch = 0;
	public int flagCapture = 0;
	public int flagRetrieve = 0;

	public boolean hasDeadChar() {
		return deadChar;
	}

	public Controller() {
		info = new CharacterInfo(CharacterClass.Agile);
	}

	public Character createNewCharacter(Vec2fi spawn) {
		if (character != null)
			character.takeDamage(new DamageInfo(0, DamageType.MISC_ONE_SHOT, new Vec2f(), 0, null));
		character = info.createNewCharacter(spawn);
		character.controller = this;
		deadChar = false;
		return character;
	}

	public Character getCharacter() {
		return character;
	}

	public CharacterInfo getCharInfo() {
		return info;
	}

	public Texture getPortrait() {
		Texture portrait;
		String path = "data/sprites/characters/" + info.getSkin();
		if ( ContentManager.resExists(path+"/head.png") )
			portrait = Main.getRenderer().loadTexture(path + "/head.png");
		else
			portrait = Main.getRenderer().loadTexture(path + "/head_tr.png");
		portrait.setFilter(false);
		return portrait;
	}

	/**
	 * Event called by the controlled Character on death
	 * 
	 * @param deathCause
	 */
	public void death(DamageInfo deathCause) {
		deadChar = true;

		// Update scores
		if (deathCause.dmgType != DamageType.MISC_ONE_SHOT)
			deaths++;
		if (deathCause.instigator instanceof Character) {
			if (((Character) deathCause.instigator).controller != null) {
				((Character) deathCause.instigator).controller.kills++;
			}
		}
		if (Main.getGameMaster().getCurrent() instanceof Game) {
			((Game) Main.getGameMaster().getCurrent()).characterDeath(this, character);
		}
		character.controller = null;
		character = null;
	}

	public void zombieChar() {
		deadChar = true;
		if(Main.getGameMaster().getCurrent() instanceof Game)
			((Game)(Main.getGameMaster().getCurrent())).evalOneLeft();
	}

	public void resetScore() {
		roundsWon = 0;
		deaths = 0;
		kills = 0;
		flagCatch = 0;
		flagCapture = 0;
		flagRetrieve = 0;
	}

	public abstract void step(double d);
}
