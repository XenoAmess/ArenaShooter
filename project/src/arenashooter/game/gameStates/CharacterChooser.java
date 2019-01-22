package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.game.GameMaster;

public class CharacterChooser extends GameState {
	
	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	
	public Collection<Controller> getControllers() {
		return controllers.values();
	}
	
	@Override
	public void init() {
		controllers.put(Device.KEYBOARD, new Controller(Device.KEYBOARD));
	}
	
	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if(Input.actionPressed(device, Action.JUMP) && !controllers.keySet().contains(device)) {
				controllers.put(device, new Controller(device));
				System.out.println("add controller");
			}
			// TODO : remove controller when B is pressed
		}
//		for (String child : map.children.keySet()) {
//			if(map.children.get(child) instanceof CharacterSprite) {
//				map.children.remove(child);
//			}
//		}
		for (int i = 0; i < GameMaster.gm.controllers.entrySet().size(); i++) {
			
		}
		if(Input.actionPressed(Device.KEYBOARD, Action.JUMP)) {
			GameMaster.gm.requestNextState();
		}
	}

}
