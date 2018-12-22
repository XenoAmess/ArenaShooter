package arenashooter.entities;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Game {
	public static Game game;
	
	private Map map;
	private Character[] players = new Character[2];
	public Camera camera;
	
	private Game() {
		map = new Map();
		
		players[0] = new Character(new Vec2f(300, 0));
		players[0].attachToParent(map, "Player 1");
		players[1] = new Character(new Vec2f(-300, 0));
		players[1].attachToParent(map, "Player 2");
		
		camera = new Camera( new Vec3f(0, 0, 450) );
		camera.attachToParent(map, "camera");
		
		Plateform plat = new Plateform(new Vec2f(0, 510), new Vec2f(5000, 20));
		plat.attachToParent(map, "Platform 1");
		
		Plateform plat2 = new Plateform(new Vec2f(-800, 220), new Vec2f(300, 300));
		plat2.attachToParent(map, "Platform 2");
		
		Plateform plat3 = new Plateform(new Vec2f(800, 220), new Vec2f(300, 300));
		plat3.attachToParent(map, "Platform 3");
		
		Plateform plat4 = new Plateform(new Vec2f(0, -450), new Vec2f(500, 20));
		plat4.attachToParent(map, "Platform 4");
		
		Particles p = new Particles(new Vec2f(0, -1000));
		p.attachToParent(map, "particles");
		
		SoundEffect testSound = new SoundEffect( new Vec2f(0, 0), "data/sound/jump.ogg" );
		testSound.setVolume(.45f);
		testSound.attachToParent(map, "testSound");
		
		Music music = new Music("data/music/Juhani Junkala [Retro Game Music Pack] Level 1.ogg", true);
		music.attachToParent(map, "music");
//		music.play();
	}
	
	public Map getMap() {
		return map;
	}
	
	public static void newGame() {
		game = new Game();
	}
	
	public void update(double d) {
		if( camera != null ) {
			camera.center(players, d);
			Audio.setListener(camera.position, camera.rotation);
		} else
			Audio.setListener( new Vec3f(), Quat.fromAngle(0) );
		
		//TODO: remove temp particle system and sound movement
		((Spatial)map.children.get("particles")).position.x = (float) (300*Math.sin(.003*System.currentTimeMillis()));
		((Spatial)map.children.get("testSound")).position = ((Spatial)map.children.get("particles")).position;
//		((SoundEffect)map.children.get("testSound")).play();
		
		map.step(d);
	}
	
	public void draw() {
		map.draw();
	}
}
