package arenashooter.game.gameStates;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.MenuPause;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.GameMaster;

public abstract class GameState {
	protected Map map;

	protected Menu menu = null;

	public GameState() {
		map = new Map();
	}

	public void init() {
		// Camera
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		Camera cam = new Camera(new Vec3f(0, 0, 850));
		cam.attachToParent(map, "camera");
		Window.setCamera(cam);
	}

	public void update(double delta) {
		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)
				| Input.actionJustPressed(Device.CONTROLLER01, Action.UI_PAUSE)
				&& (GameMaster.current instanceof Game)) {
			if (menu == null)
				menu = new MenuPause();
			else
				menu = null;
		}
		if (menu != null)
			menu.update();
		map.step(delta);
	}

	public void draw() {
		map.drawSelfAndChildren();

		if (menu != null)
			menu.draw();

		Window.endTransparency(); // Make sure to end transparency
	}

	public Map getMap() {
		return map;
	}
}
