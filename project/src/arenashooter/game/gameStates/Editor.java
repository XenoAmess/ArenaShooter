package arenashooter.game.gameStates;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Stack;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.editorEnum.Prime;
import arenashooter.game.gameStates.editorEnum.SetMesh;

public class Editor extends GameState {

	private Vec2f forVisible = new Vec2f(-64, 0), forNotVisible = new Vec2f(-110, 0);
	private boolean primeVisible = true;

	/* Save-quit Menu */
	private MenuSelectionV<UiActionable> saveQuitMenu = new MenuSelectionV<>(5, 0, 0, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");

	/* Set Menu */
	private MenuSelectionV<UiActionable> setMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	/* Add Menu */
	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	private InputListener inputs = new InputListener();
	private Camera cam;

	/* Main Menu */
	private MultiMenu<Prime> multiMenu = new MultiMenu<>(10, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));

	/* Sub add menu */
	private MenuSelectionV<UiActionable> meshMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	/* Bg Menu */
	private Menu bg = new Menu(2);

	/* Menu Set entity */
	private MenuSelectionV<UiActionable> setEntity = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");
	private Label setEntityLabel = new Label(0, new Vec2f(15), "Empty");
	private ScrollerH<SetMesh> setEntityScroller = new ScrollerH<>(0, new Vec2f(35, 25), SetMesh.values());

	private Stack<Navigable> stackMenu = new Stack<>();

	private LinkedList<Mesh> meshList = new LinkedList<>();

	public Editor() {
		super(1);
		final Vec2f scale = new Vec2f(15, 5);

		/* Save-Quit Menu */
		saveQuitMenu.setEcartement(8);
		Button save = new Button(0, scale, "Save");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		save.setScaleText(new Vec2f(20));
		Button quit = new Button(0, scale, "Quit");
		quit.setOnArm(new Trigger() {
			
			@Override
			public void make() {
				GameMaster.gm.requestNextState(new MenuStart(), GameMaster.mapEmpty);
			}
		});
		quit.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		quit.setScaleText(new Vec2f(20));
		saveQuitMenu.addElementInListOfChoices(save, 1);
		saveQuitMenu.addElementInListOfChoices(quit, 1);

		/* Add Menu */
		final float scaleText = 20f;
		Button entity = new Button(0, scale, "entity");
		entity.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(entity, 1);
		Button rigid = new Button(0, scale, "rigid");
		rigid.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(rigid, 1);
		Button mesh = new Button(0, scale, "mesh");
		mesh.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(mesh, 1);
		Button text = new Button(0, scale, "text");
		text.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(text, 1);
		Button statiq = new Button(0, scale, "static");
		statiq.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(statiq, 1);
		Button jointPin = new Button(0, scale, "jointPin");
		jointPin.setScaleText(new Vec2f(scaleText));
		addMenu.addElementInListOfChoices(jointPin, 1);
		addMenu.setEcartement(8);

		/* Mesh menu */
		meshMenu.setPositionRef(new Vec2f(forVisible.x, -40));
		meshMenu.setEcartement(6);
		meshMenuConstruction();

		/* Set Menu */
		setMenu.setEcartement(6);

		/* set entity menu */
		setEntity.addUiElement(setEntityLabel, 1);
		setEntity.setPosition(forVisible);
		setEntity.addElementInListOfChoices(setEntityScroller, 1);
		setEntity.setPositionRef(new Vec2f(forVisible.x, -32));
		setEntityLabel.setPos(new Vec2f(forVisible.x, -43));
		setEntityLabel.setScale(new Vec2f(20));
		setEntityScroller.setAlwaysScrollable(true);

		/* Main Menu */
		Rectangle bg = new Rectangle(0, new Vec2f(50, 150), new Vec4f(0.5, 0.5, 0.5, 0.2));
		this.bg.setBackground(bg);
		this.bg.setPosition(forVisible);
		multiMenu.addMenu(setMenu, Prime.Set);
		multiMenu.addMenu(addMenu, Prime.Add);
		multiMenu.addMenu(saveQuitMenu, Prime.Exit);
		multiMenu.setPosition(forVisible);
		multiMenu.setPositionRef(new Vec2f(forVisible.x, -30));

		stackMenu.push(multiMenu);

		mesh.setOnArm(new Trigger() {

			@Override
			public void make() {
				stackMenu.push(meshMenu);
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						stackMenu.peek().leftAction();
						break;
					case UI_RIGHT:
						stackMenu.peek().rightAction();
						break;
					case UI_UP:
						stackMenu.peek().upAction();
						break;
					case UI_DOWN:
						stackMenu.peek().downAction();
						break;
					case UI_OK:
						stackMenu.peek().selectAction();
						break;
					case UI_CONTINUE:
						setPrimeVisible(!primeVisible);
						break;
					case UI_BACK:
						if (stackMenu.size() > 1) {
							stackMenu.pop();
						}
						break;
					default:
						break;
					}
				} else if (event.getActionState() == ActionState.PRESSED) {
					if (stackMenu.peek() == setEntity) {
						final double scaleSpeed = 0.005 , positionSpeed = 0.02;
						switch (event.getAction()) {
						case UI_DOWN2:
							Entity e = current.getChild(setEntityLabel.getText());
							if (meshList.contains(e)) {
								Mesh m = (Mesh) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									m.localPosition.add(new Vec3f(0, positionSpeed, 0));
									break;
								case SCALE:
									m.scale.add(new Vec3f(0, -scaleSpeed, 0));
									break;
								case ROTATION:
									m.localRotation.rotate(new Vec3f(0, 0.1, 0));
									break;
								default:
									break;
								}
							}
							break;
						case UI_UP2:
							e = current.getChild(setEntityLabel.getText());
							if (meshList.contains(e)) {
								Mesh m = (Mesh) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									m.localPosition.add(new Vec3f(0, -positionSpeed, 0));
									break;
								case SCALE:
									m.scale.add(new Vec3f(0, scaleSpeed, 0));
									break;
								case ROTATION:
									m.localRotation.rotate(new Vec3f(0, -0.1, 0));
									break;
								default:
									break;
								}
							}
							break;
						case UI_LEFT2:
							e = current.getChild(setEntityLabel.getText());
							if (meshList.contains(e)) {
								Mesh m = (Mesh) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									m.localPosition.add(new Vec3f(-positionSpeed, 0, 0));
									break;
								case SCALE:
									m.scale.add(new Vec3f(-scaleSpeed, 0, 0));
									break;
								case ROTATION:
									m.localRotation.rotate(new Vec3f(-0.1, 0, 0));
									break;
								default:
									break;
								}
							}
							break;
						case UI_RIGHT2:
							e = current.getChild(setEntityLabel.getText());
							if (meshList.contains(e)) {
								Mesh m = (Mesh) e;
								switch (setEntityScroller.get()) {
								case POSITION:
									m.localPosition.add(new Vec3f(positionSpeed, 0, 0));
									break;
								case SCALE:
									m.scale.add(new Vec3f(scaleSpeed, 0, 0));
									break;
								case ROTATION:
									m.localRotation.rotate(new Vec3f(0.1, 0, 0));
									break;
								default:
									break;
								}
							}
							break;
						default:
							break;
						}
					}
				}
			}
		});

	}

	private void meshMenuConstruction() {
		File root = new File("data");
		LinkedList<File> m = new LinkedList<>();
		m.addAll(allMesh(root));

		for (File file : m) {
			String name = file.getName();
			Button b = new Button(0, new Vec2f(30, 5.5), name.substring(0, name.length() - 4));
			meshMenu.addElementInListOfChoices(b, 1);
			b.setScaleText(new Vec2f(20));
			b.setOnArm(new Trigger() {

				@Override
				public void make() {
					String replace = file.getPath().replace('\\', '/');
					Mesh mesh = new Mesh(new Vec3f(), replace);
					String name = mesh.genName();
					mesh.attachToParent(current, name);
					meshList.add(mesh);
					Button b = new Button(0, new Vec2f(30, 5.5), name);
					b.setScaleText(new Vec2f(15));
					setMenu.addElementInListOfChoices(b, 1);
					b.setOnArm(new Trigger() {

						@Override
						public void make() {
							setEntityLabel.setText(name);
							stackMenu.push(setEntity);
						}
					});
					b.arm();
				}
			});
		}
	}

	private LinkedList<File> allMesh(File parent) {
		LinkedList<File> mesh = new LinkedList<>();
		for (File file : parent.listFiles()) {
			if (file.getName().endsWith(".obj")) {
				mesh.add(file);
			} else if (file.isDirectory()) {
				mesh.addAll(allMesh(file));
			}
		}
		return mesh;
	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void setPrimeVisible(boolean visible) {
		if (visible) {
			bg.setPositionLerp(forVisible, 40);
			stackMenu.peek().setPositionLerp(forVisible, 30);
		} else {
			bg.setPositionLerp(forNotVisible, 30);
			stackMenu.peek().setPositionLerp(forNotVisible, 40);
		}
		primeVisible = visible;
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		// TODO : Ajouter les entites au setMenu
		stackMenu.peek().update(delta);
		bg.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		bg.draw();
		stackMenu.peek().draw();
		Window.endUi();
	}

}
