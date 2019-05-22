package arenashooter.engine.events;

import java.util.LinkedList;
import java.util.Observable;

/**
 * @author Nathan
 * Boolean value that launch an event NewValueEvent in case of a value changement
 */
public class BooleanProperty extends Observable {
	private boolean value = true;
	public LinkedList<EventListener<NewValueEvent<Boolean>>> listener = new LinkedList<>();
	
	/**
	 * @param newValue
	 * Change the value of the property and launch the events associated
	 */
	public void setValue(boolean newValue) {
		value = newValue;
		for (EventListener<NewValueEvent<Boolean>> event : listener) {
			event.action(new NewValueEvent<>(!newValue, newValue));
		}
	}
	
	public boolean getValue() {
		return value;
	}
}