package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.UiElement;

public abstract class UiSimpleElementNavigable extends UiElement {

	public UiSimpleElementNavigable(double rot, Vec2f scale) {
		super(rot, scale);
	}

	@Override
	public void selectAction() {
		// Nothing
	}

	@Override
	public boolean isSelected() {
		return false;
	}
	
	@Override
	public void unSelec() {
		// Nothing
	}
	
	@Override
	public void downAction() {
		owner.exit.launch(new MenuExitEvent(Side.Down));
	}

	@Override
	public void leftAction() {
		owner.exit.launch(new MenuExitEvent(Side.Left));
	}

	@Override
	public void rightAction() {
		owner.exit.launch(new MenuExitEvent(Side.Right));
	}

	@Override
	public void upAction() {
		owner.exit.launch(new MenuExitEvent(Side.Up));
	}

}
