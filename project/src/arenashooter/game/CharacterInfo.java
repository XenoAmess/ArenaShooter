package arenashooter.game;

import arenashooter.engine.math.Vec2fi;
import arenashooter.entities.spatials.Character;

public class CharacterInfo {
	private CharacterClass charClass;
	int skin = 0;
	boolean mirrorLeftFoot;
	
	public CharacterInfo(CharacterClass charClass) {
		this.charClass = charClass;
	}
	
	public void previousClass() {
		skin = 0;
		charClass = charClass.previousClass();
	}
	
	public boolean mirrorLeftFoot() {
		return getSkin() == "poisson_01";
	}
	
	public void nextClass() {
		skin = 0;
		charClass = charClass.nextClass();
	}
	
	public void previousSkin() {
		skin = charClass.previousSkin(skin);
	}
	
	public void nextSkin() {
		skin = charClass.nextSkin(skin);
	}
	
	public String getSkin() {
		return charClass.getSkin(skin);
	}
	
	public CharacterClass getCharClass() {
		return charClass;
	}

	public Character createNewCharacter(Vec2fi spawn) {
		return new Character(spawn, this , false);
	}

}
