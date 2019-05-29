package arenashooter.engine.ui;

import java.util.Stack;

import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuSelectionV<E extends UiElement> extends Menu implements Navigable {
	private UiImage selec;
	private float ecartement = 5;
	private Vec2f positionRef = new Vec2f();
	private int index = 0;
	private Stack<E> elements = new Stack<>();
	public boolean selectorVisible = true;
	private boolean loop = true;

	public MenuSelectionV(int maxLayout, float x, float y, Vec2f scaleSelec, String pathTextureSelec) {
		super(maxLayout);
		Texture t = Texture.loadTexture(pathTextureSelec);
		t.setFilter(false);
		selec = new UiImage(0, scaleSelec, t, new Vec4f(1, 1, 1, 1));
		selec.setVisible(true);
		Vec2f pos = new Vec2f(x, y);
		setPosition(pos);
		setImageSelec(selec, 2);
	}

	public void setEcartement(float e) {
		ecartement = e;
	}

	public void addElementInListOfChoices(E element, int layout) {
		if (elements.contains(element))
			return;
		Vec2f newPosition = new Vec2f(positionRef.x, positionRef.y + ecartement * elements.size());
		if (elements.isEmpty()) {
			selec.setPos(newPosition);
		}
		if (!elements.contains(element)) {
			elements.add(element);
		}
		element.setPos(newPosition);
		element.setVisible(selectorVisible);
		restart();
	}

	public void removeElementInListOfChoices(E element) {
		if (elements.contains(element)) {
			elements.remove(element);
			if (element.getPos().x == selec.getPos().x && element.getPos().y == selec.getPos().y) {
				selec.setPos(elements.peek().getPos());
			}
			element.setVisible(false);
		}
	}

	public void downAction() {
		if (loop) {
			int save = index;
			index++;
			if (index >= elements.size()) {
				index = 0;
			}
			while (index != save && !getTarget().isVisible()) {
				index++;
				if (index >= elements.size()) {
					index = 0;
				}
			}
		} else {
			index++;
			if (index >= elements.size()) {
				if (elements.size() == 0) {
					index = 0;
				} else {
					index = elements.size() - 1;
				}
				exit.launch(new MenuExitEvent(Side.Down));
				return;
			}
			while (!getTarget().isVisible()) {
				index++;
				if (index >= elements.size()) {
					if (elements.size() == 0) {
						index = 0;
					} else {
						index = elements.size() - 1;
					}
					exit.launch(new MenuExitEvent(Side.Down));
					return;
				}
			}
		}
		majSelecPosition();
	}

	public void upAction() {
		if (loop) {
			int save = index;
			index--;
			if (index < 0) {
				if (elements.size() != 0) {
					index = elements.size() - 1;
				} else {
					index = 0;
				}
			}
			while (index != save && !getTarget().isVisible()) {
				index--;
				if (index < 0) {
					index = elements.size() - 1;
				}
			}
		} else {
			index--;
			if (index < 0) {
				index = 0;
				exit.launch(new MenuExitEvent(Side.Up));
				return;
			}
			while (!getTarget().isVisible()) {
				index--;
				if (index < 0) {
					index = 0;
					exit.launch(new MenuExitEvent(Side.Up));
					return;
				}
			}
		}
		majSelecPosition();
	}

	public void rightAction() {
		if (getTarget() != null && getTarget().isSelected()) {
			getTarget().rightAction();
		} else {
			exit.launch(new MenuExitEvent(Side.Right));
		}
	}

	public void leftAction() {
		if (getTarget() != null && getTarget().isSelected()) {
			getTarget().leftAction();
		} else {
			exit.launch(new MenuExitEvent(Side.Left));
		}
	}

	public void setPositionRef(Vec2f position) {
		positionRef.set(position);
		for (int i = 0; i < elements.size(); i++) {
			E element = elements.get(i);
			element.setPos(new Vec2f(positionRef.x, positionRef.y + ecartement * i));
		}
		majSelecPosition();
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(newPosition, getPosition());
		super.setPosition(newPosition);
		setPositionRef(Vec2f.add(positionRef, dif));
	}

	@Override
	public void setPositionLerp(Vec2f position , double lerp) {
		Vec2f dif = Vec2f.subtract(position, getPosition());
		positionRef.set(Vec2f.add(positionRef, dif));
		super.setPositionLerp(position , lerp);
	}

	public void setImageSelec(UiImage image, int layout) {
		addUiElement(image, layout);
		selec = image;
		selec.setPos(positionRef.clone());
	}

	public E getTarget() {
		if (index < elements.size() && index >= 0) {
			return elements.get(index);
		} else {
			return null;
		}
	}

	public void restart() {
		index = 0;
		E e = elements.get(index);
		if (e != null) {
			selec.setPos(e.getPos());
		}
	}

	@Override
	public void setVisible(boolean visible) {
		for (E e : elements) {
			e.setVisible(visible);
		}
		super.setVisible(visible);
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	protected void majSelecPosition() {
		E target = getTarget();
		if (target != null) {
			selec.setPositionLerp(getTarget().getPos() , 40);
		}
	}

	@Override
	public void draw() {
		if (isVisible()) {
			super.draw();
			for (E e : elements) {
				if (e.isVisible()) {
					e.draw();
				}
			}
			if (selectorVisible) {
				selec.draw();
			}
		}
	}

	@Override
	public void update(double delta) {
		int i = 0;
		for (E e : elements) {
			if (e.isVisible()) {
				e.setPositionLerp(new Vec2f(positionRef.x, positionRef.y + ecartement * i) , 20);
				i++;
			}
			e.update(delta);
		}
		selec.setVisible(selectorVisible);
		super.update(delta);
	}

	@Override
	public void selectAction() {
		getTarget().selectAction();
	}

	@Override
	public boolean isSelected() {
		return getTarget().isSelected();
	}

	@Override
	public void unSelec() {
		getTarget().unSelec();
	}
}
