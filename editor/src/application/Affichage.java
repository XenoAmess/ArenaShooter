package application;

import application.propertiestabs.PropertiesTab;
import gamedata.entities.Entity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import math.Vec2;

public final class Affichage {
	//Right menu
	public static GridSnap gridSnap;
	public static SceneTree sceneTree;
	public static ScrollPane propertiesContainer;
	private static Label selectedName;

	public static BorderPane root = new BorderPane();
	
	public static Entity selected = null;
	
	//Used for context-menu entity spawning
	private static Vec2 contextMenuLoc = new Vec2();
	
	//Center view
	private static ScrollPane scrollContainer;
	private static double scrollInitialH, scrollInitialV;
	private static Point2D dragAnchor;
	private static double zoom = 1;

	private Affichage() { }
	
	static {
		//MenuBar
		createMenuBar();		
		
		//Bottom
		createBottomBar();
		
		// Right
		createRightMenu();

		// Center
		createCenterView();
	}
	
	public static double getZoom() { return zoom; }
	
	public static void setZoom(double newZoom) {
		zoom = Math.max(.2, Math.min(newZoom, 3));
		
		ListEntite.view.setScaleX(zoom);
		ListEntite.view.setScaleY(zoom);
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
		
		//Upadte displayed name
		refreshSelectionTitle();
		
		//Add glow around newly selected object and push it to front
		Node visual = ListEntite.visuals.get(selected);
		if(visual != null)  {
			visual.setStyle("-fx-effect: dropshadow(two-pass-box, rgba(255,200,0,1), 4, 3, 0, 0);");
			visual.toFront();
		}
	}
	
	public static void refreshSelectionTitle() {
		if(selected == null)
			selectedName.setText("World properties");
		else
			selectedName.setText(selected.name);
	}

	private static void createCenterView() {
		ListEntite.view.setBackground(new Background(new BackgroundFill(Color.rgb(56, 56, 56), new CornerRadii(0), new Insets(0))));
		scrollContainer = new ScrollPane();
		scrollContainer.setContent(ListEntite.view);
		scrollContainer.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		ListEntite.view.setPadding(new Insets(300));
		root.setCenter(scrollContainer);

		//Middle-mouse drag
		ListEntite.view.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.isMiddleButtonDown()) { //Middle click
					//Pour garder le point de depart en memoire
					scrollInitialH = scrollContainer.getHvalue();
					scrollInitialV = scrollContainer.getVvalue();
					dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
				}
			}
		});
		ListEntite.view.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {//TODO: Fix movement scaling
				if(!me.isMiddleButtonDown()) return; //Not middle click
				
				//Calcule position apres le drag
				double dragX = me.getSceneX() - dragAnchor.getX();
				double dragY = me.getSceneY() - dragAnchor.getY();
				scrollContainer.setHvalue( scrollInitialH - 1.75*dragX/ListEntite.view.getWidth() );
				scrollContainer.setVvalue( scrollInitialV - 1.75*dragY/ListEntite.view.getHeight() );
			}
		});
		
		ListEntite.view.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if(event.isControlDown()) { //Only zoom if control is pressed
					event.consume(); //Consume event to prevent scrolling
					setZoom(zoom + event.getDeltaY()/1000);
				}
			}
		});
		
		//Context Menu
		ContextMenu contextMenu = new ContextMenu();

		//Add platform
		MenuItem contextAddPlatform = new MenuItem("Add platform");
		contextAddPlatform.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ListEntite.newPlatform( contextMenuLoc );
			}
		});

		contextMenu.getItems().addAll(contextAddPlatform);

		ListEntite.view.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				contextMenuLoc.x = event.getX();
				contextMenuLoc.y = event.getY();
				contextMenu.show(ListEntite.view, event.getScreenX(), event.getScreenY());
			}
		});
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
				ListEntite.newPlatform();
			}
		});
		menuAdd.getItems().addAll(menuAddEntity, menuAddPlatform);

		MenuBar mb1 = new MenuBar(menuFile, menuAdd);
		root.setTop(mb1);
	}
	
	private static void createBottomBar() {
		gridSnap = new GridSnap(10);
		Button zoomOut = new Button("Reset zoom");
		zoomOut.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setZoom(1);
			}
		});
		HBox bottomBar = new HBox(10, gridSnap, zoomOut);
		root.setBottom(bottomBar);
	}
	
	private static void createRightMenu() {
		sceneTree = new SceneTree();
		
		Separator separator = new Separator();
		
		selectedName = new Label("");
		selectedName.setFont((Font.font("Arial", FontWeight.BOLD, 14)));

		propertiesContainer = new ScrollPane();
		propertiesContainer.setFitToWidth(true);
		propertiesContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		propertiesContainer.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		propertiesContainer.setPadding(new Insets(5));

		VBox vBox = new VBox(10, sceneTree, separator, selectedName, propertiesContainer);
		vBox.setBorder(new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, new CornerRadii(1),
				new BorderWidths(1), new Insets(3))));
		
		root.setRight(vBox);
	}
}
