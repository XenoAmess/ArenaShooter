package application;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ListEntite {

	private static HashMap<Rectangle, Entite> entites = new HashMap<>();
	protected static double initX;
	protected static double initY;
	private static double scale = 5;
	protected static Point2D dragAnchor;
	private static Rectangle character = new Rectangle(50, 120, Color.RED);
	static StackPane pane = new StackPane(character);

	private ListEntite() {
	}

	/**
	 * Rectangle repr�sentant la taille du collider d'un Character de SuperBlep
	 * 
	 * @return Un rectangle
	 */
	public static Rectangle getRecChar() {
		return character;
	}

	/**
	 * Cr�e un rectangle bougeable avec la souris et une entite qui lui est associ�
	 * dans la map d'entite
	 */
	public static void newPlateforme() {
		Rectangle nw = newRectangleSuitSouris(300, 20, Color.YELLOW);
		pane.getChildren().add(nw);
		Entite e = new Entite(Entite.Type.Plateforme, 0, 0, nw.getWidth() / 2, nw.getHeight() / 2);
		entites.put(nw, e);
	}

	/**
	 * Cr�e un rectangle qui suit la souris lors d'un click and drag
	 * @param x longueur
	 * @param y hauteur
	 * @param c couleur
	 * @return Un rectangle
	 */
	private static Rectangle newRectangleSuitSouris(double x, double y, Paint c) {
		Rectangle rec = new Rectangle(x, y, c);
		rec.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// Pour garder le point de depart en memoire
				initX = rec.getTranslateX();
				initY = rec.getTranslateY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
			}
		});
		rec.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// calcule position apres le drag
				double dragX = me.getSceneX() - dragAnchor.getX();
				double dragY = me.getSceneY() - dragAnchor.getY();
				double newXPosition = initX + dragX;
				double newYPosition = initY + dragY;
				
				// restriction de fenetre
				if (newXPosition <= pane.getWidth() - rec.getX()) {
					rec.setTranslateX(newXPosition);
				}
				if (newYPosition <= pane.getHeight() - rec.getY()) {
					rec.setTranslateY(newYPosition);
				}
				
				// On change la position de l'entit� li�e
				Entite e = entites.get(rec);
				e.xPosition += dragX / scale;
				e.yPosition += dragY / scale;
			}
		});
		return rec;
	}
}
