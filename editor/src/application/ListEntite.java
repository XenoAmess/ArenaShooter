package application;

import java.util.HashMap;

import gamedata.entities.Entity;
import gamedata.entities.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import math.Vec2;

public class ListEntite {

	private static HashMap<Rectangle, Entite> entites = new HashMap<>();
	protected static double initX;
	protected static double initY;
	private static double scale = 5;
	protected static Point2D dragAnchor;
	private static Rectangle character = new Rectangle(50, 120, Color.RED);
	static Pane pane = new Pane(character);

	private ListEntite() {
	}

	/**
	 * Rectangle représentant la taille du collider d'un Character de SuperBlep
	 * 
	 * @return Un rectangle
	 */
	public static Rectangle getRecChar() {
		return character;
	}
	
	public static HashMap<Rectangle, Entite> getHashMapEntites(){
		return entites;
	}

	/**
	 * Create a platform and its movable rectangle in the scene view
	 */
	public static void newPlateforme() {
		Platform entity = new Platform(new Vec2(), 0, new Vec2(150, 10));
		entity.name = "Platform "+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
		
		//Create movable rectangle attached to the platform
		pane.getChildren().add(new MovableRectangle(entity, new Vec2(entity.extent.x*2, entity.extent.y*2), Color.YELLOW));
	}
	
	/**
	 * Create an entity
	 */
	public static void newEntity() {
		Entity entity = new Entity();
		entity.name = "Entity "+String.valueOf(System.currentTimeMillis());
		entity.createProperties();
		Main.map.children.put(entity.name, entity);
		Affichage.sceneTree.addEntity(entity);
		Affichage.selectEntity(entity);
	}
}