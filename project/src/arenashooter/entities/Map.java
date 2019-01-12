package arenashooter.entities;

import java.util.ArrayList;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.Disk;
import arenashooter.engine.physic.shapes.Rectangle;
import arenashooter.entities.spatials.Plateform;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.ItemCounter;
import arenashooter.entities.spatials.items.WeaponsC;
import arenashooter.entities.spatials.items.WeaponsD;
import arenashooter.entities.spatials.items.Item.ItemSprite;

public class Map extends Entity {

	public ArrayList<Vec2f> spawn;
	public Vec2f gravity;

	/**
	 * world bounds (min x, min y, max x, max y)
	 */
	public Vec4f cameraBounds;
	
	public Map(ArrayList<Plateform> plateform) {
		int i = 0;
		for (Plateform p : plateform) {
			i++;
			p.attachToParent(this, "Plateforme"+i);
		}
		testPhysics();
	}

	public Map(int nbPlayer) {
		spawn = new ArrayList<>(nbPlayer);
		creationPlateforme();
		creationSpawn(nbPlayer);

		Mesh testMesh = new Mesh(new Vec3f(0, -1000, -2000), new Quat(), new Vec3f(2000), "data/meshes/Suzanne.obj");
		testMesh.attachToParent(this, "aaMesh_Suzanne");
		Mesh arrows = new Mesh(new Vec3f(-500, 500, 200), new Quat(), new Vec3f(200), "data/meshes/arrows.obj");
		arrows.attachToParent(this, "Mesh Arrows");

		cameraBounds = new Vec4f(-5000, -1000, 5000, 1000);
	}

	private void creationSpawn(int nbPlayer) {
		for (int i = 0; i < nbPlayer; i++) {
			spawn.add(i, new Vec2f(i * 200 - 300, 0));
		}
	}
	
	private void testPhysics() {
		//Rigid body 1
		Vec2f position = new Vec2f(-450, -500);
		RigidBody body = new RigidBody(new Rectangle(new Vec2f(100, 50)), position, .5, 500);
		RigidBodyContainer rb = new RigidBodyContainer(position, body);
		Sprite rbSprite = new Sprite(new Vec2f(), "data/default_texture.png");
		rbSprite.size = new Vec2f(200, 100);
		rb.attachToParent(this, "Rigid Body test");
		rbSprite.attachToParent(rb, "Sprite");
		
		//Rigid body 2
		position = new Vec2f(-400, -675);
		body = new RigidBody(new Disk(50), position, 0, 100);
		rb = new RigidBodyContainer(position, body);
		rbSprite = new Sprite(new Vec2f(), "data/sprites/UnMoineHD.png");
		rbSprite.size = new Vec2f(100, 100);
		rb.attachToParent(this, "Rigid Body test 2");
		rbSprite.attachToParent(rb, "Sprite");
	}

	private void creationPlateforme() {
		// map 1 parfaitement pensee

		Plateform plat = new Plateform(new Vec2f(0, 510), new Vec2f(1500, 100));
		plat.attachToParent(this, "Platform 1");

		Mesh plat1Mesh = new Mesh(new Vec3f(0, 410, 0), new Quat(), new Vec3f(5),
				"data/meshes/catwalk/catwalk_200.obj");
		plat1Mesh.textures[0] = Texture.loadTexture("data/meshes/catwalk/catwalk_top.png");
		plat1Mesh.textures[0].setFilter(false);
		plat1Mesh.attachToParent(this, "Platform Mesh 1.1");
		plat1Mesh = new Mesh(new Vec3f(1000, 410, 0), new Quat(), new Vec3f(5), "data/meshes/catwalk/catwalk_200.obj");
		plat1Mesh.textures[0] = Texture.loadTexture("data/meshes/catwalk/catwalk_top.png");
		plat1Mesh.textures[0].setFilter(false);
		plat1Mesh.attachToParent(this, "Platform Mesh 1.2");

		Plateform plat2 = new Plateform(new Vec2f(-800, 210), new Vec2f(300, 300));
		plat2.attachToParent(this, "Platform 2");

		Plateform plat3 = new Plateform(new Vec2f(1500, -150), new Vec2f(300, 25));
		plat3.attachToParent(this, "Platform 3");

		Plateform plat4 = new Plateform(new Vec2f(-2000, -400), new Vec2f(300, 25));
		plat4.attachToParent(this, "Platform 4");

		Plateform plat5 = new Plateform(new Vec2f(-1500, 800), new Vec2f(300, 25));
		plat5.attachToParent(this, "Platform 5");

		Plateform plat6 = new Plateform(new Vec2f(-2500, 1100), new Vec2f(300, 25));
		plat6.attachToParent(this, "Platform 6");

		Plateform plat7 = new Plateform(new Vec2f(1900, 800), new Vec2f(300, 25));
		plat7.attachToParent(this, "Platform 7");

		// Plateform plat8 = new Plateform(new Vec2f(0, -500), new Vec2f(300, 25));
		// plat8.attachToParent(this, "Platform 8");

		Plateform plat9 = new Plateform(new Vec2f(-500, 1700), new Vec2f(700, 50));
		plat9.attachToParent(this, "Platform 9");

		Plateform plat10 = new Plateform(new Vec2f(1300, 1300), new Vec2f(300, 25));
		plat10.attachToParent(this, "Platform 10");

		Plateform plat11 = new Plateform(new Vec2f(-2700, -2700), new Vec2f(2000, 2000));
		plat11.attachToParent(this, "Platform 11");

		Plateform plat12 = new Plateform(new Vec2f(0, 2200), new Vec2f(5000, 10));// 7000 //C'est quoi ce commentare ?
		plat12.attachToParent(this, "Platform 12");
		
		Vec2f p =new Vec2f(-300, 2200);
		WeaponsC item11 = new WeaponsC(p, ItemSprite.minugun);
		item11.attachToParent(this, ItemCounter.weapon());
		
		WeaponsC item111 = new WeaponsC(new Vec2f(300, 2200),ItemSprite.assault);
		item111.attachToParent(this, ItemCounter.weapon());
		
		WeaponsC item1111 = new WeaponsC(new Vec2f(900, 2200),ItemSprite.armor);
		item1111.attachToParent(this, ItemCounter.armor());
//
//		Plateform plat13 = new Plateform(new Vec2f(0, -450), new Vec2f(500, 20));
//		plat13.attachToParent(this, "Platform 13");
//
//		Plateform plat14 = new Plateform(new Vec2f(-1900, 1200), new Vec2f(150, 20));
//		plat14.attachToParent(this, "Platform 14");

//		Bullet bul1 = new Bullet(new Vec2f(1000, 350), new Vec2f(-300, 0));
//		bul1.attachToParent(this, "bul1");
//
//		Bullet bul2 = new Bullet(new Vec2f(500, 70), new Vec2f(-400, 100));
//		bul2.attachToParent(this, "bul2");
//
//		Bullet bul3 = new Bullet(new Vec2f(1000, 150), new Vec2f(-1200, 200));
//		bul3.attachToParent(this, "bul3");
//
//		Bullet bul4 = new Bullet(new Vec2f(500, 50), new Vec2f(-500, 170));
//		bul4.attachToParent(this, "bul4");
//
//		Bullet bul5 = new Bullet(new Vec2f(500, 20), new Vec2f(-200, 300));
//		bul5.attachToParent(this, "bul5");
//
//		Bullet bul6 = new Bullet(new Vec2f(400, 50), new Vec2f(-200, 100));
//		bul6.attachToParent(this, "bul6");
//
//		Bullet bul7 = new Bullet(new Vec2f(500, 100), new Vec2f(-200, 50));
//		bul7.attachToParent(this, "bul7");
//
//		Bullet bul8 = new Bullet(new Vec2f(500, 100), new Vec2f(-170, 30));
//		bul8.attachToParent(this, "bul8");
//
//		Bullet bul9 = new Bullet(new Vec2f(500, 100), new Vec2f(-50, 15));
//		bul9.attachToParent(this, "bul9");
//
//		Bullet bul0 = new Bullet(new Vec2f(750, 0), new Vec2f(-200, 50));
//		bul0.attachToParent(this, "bul0");
	}
	
	public static WeaponsC argl = new WeaponsC(new Vec2f(750, 300), Item.ItemSprite.assault);
	//déso, ces lignes sont temporaires
	public void addWeapons() {
		argl.attachToParent(this, "weapon1");
	}
	//TODO à supprimer
	
	public void addPlateform(Vec2f position , Vec2f extent , String nom) {
		children.put(nom, new Plateform(position, extent));
		
	}
}