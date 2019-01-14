package gamedata.entities;

import application.propertiestabs.PlatformProperties;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import math.Vec2;

public class Platform extends Spatial {
	
	public Vec2 extent;

	public Platform(Vec2 position, double rotation, Vec2 extent) {
		super(position, rotation);
		this.extent = extent.clone();
	}
	
	@Override
	public void createProperties() {
		properties = new PlatformProperties(this);
	}
	
	@Override
	public Node getIcon() {
		return new ImageView( new Image("file:editor_data/icons/platform.png"));
	}

}
