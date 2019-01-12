package application;

import application.propertiestabs.PropertiesTab;
import gamedata.entities.Entity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Affichage {
	//Right menu
	public static GridSnap gridSnap;
	public static SceneTree sceneTree;
	public static ScrollPane propertiesContainer;

	private static BorderPane root;
	
	public static Entity selected = null;

	public Affichage(BorderPane borderPane) {
		root = borderPane;
	}
	
	/**
	 * Change global selected entity
	 * @param e
	 */
	public static void selectEntity(Entity e) {
		//Remove glow from previously selected object
		Node previousVisual = ListEntite.visuals.get(selected);
		if(previousVisual != null) 
			previousVisual.setStyle("-fx-effect: none;");
		
		selected = e;
		if(e == null) {
			if(Main.map != null)
				propertiesContainer.setContent(Main.map.propertiesTab);
			else
				propertiesContainer.setContent(null);
		} else {
			sceneTree.setSelected(e);
			propertiesContainer.setContent(e.properties);
		}
		
		if((PropertiesTab)propertiesContainer.getContent() != null)
			((PropertiesTab)propertiesContainer.getContent()).update();
		
		//Add glow around newly selected object and push it to front
		Node visual = ListEntite.visuals.get(selected);
		if(visual != null)  {
			visual.setStyle("-fx-effect: dropshadow(two-pass-box, rgba(255,200,0,1), 4, 3, 0, 0);");
			visual.toFront();
		}
	}

	public void make() {
		//MenuBar
		createMenuBar();		
		
		//Bottom
		createBottomBar();
		
		// Right
		createRightMenu();

		// Center
		ListEntite.pane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
		ScrollPane scrollContainer = new ScrollPane();
		scrollContainer.setContent(ListEntite.pane);
		scrollContainer.setFitToHeight(true);
		scrollContainer.setFitToWidth(true);
		root.setCenter(scrollContainer);
	}
	
	private static void createMenuBar() {
		//File
		Menu menuFile = new Menu("_File");
		MenuItem menuFileSave = new MenuItem("_Save");
		menuFileSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Enregistreur.enregistrer();
			}
		});
		menuFile.getItems().addAll(menuFileSave);

		//Add entity
		Menu menuAdd = new Menu("_Add...");
		MenuItem menuAddEntity = new MenuItem("_Entity");
		menuAddEntity.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newEntity();
			}
		});
		MenuItem menuAddPlatform = new MenuItem("_Platform");
		menuAddPlatform.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ListEntite.newPlateforme();
			}
		});
		menuAdd.getItems().addAll(menuAddEntity, menuAddPlatform);

		MenuBar mb1 = new MenuBar(menuFile, menuAdd);
		root.setTop(mb1);
	}
	
	private static void createBottomBar() {
		gridSnap = new GridSnap(10);
		Button zoomOut = new Button("Zoom out");
		zoomOut.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//TODO: zoom out
				Enregistreur.erreur();
			}
		});
		HBox bottomBar = new HBox(10, gridSnap, zoomOut);
		root.setBottom(bottomBar);
	}
	
	private static void createRightMenu() {
		sceneTree = new SceneTree();

		propertiesContainer = new ScrollPane();
		propertiesContainer.setFitToWidth(true);
		propertiesContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		propertiesContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		VBox vBox = new VBox(10, sceneTree, propertiesContainer);
		vBox.setBorder(new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, new CornerRadii(1),
				new BorderWidths(1), new Insets(3))));
		
		root.setRight(vBox);
	}
}
