package arenashooter.game.gameStates.editor;

import java.util.HashMap;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.xmlReaders.writer.MapXmlWriter;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.MenuStart;
import arenashooter.game.gameStates.editor.editorEnum.Prime;
import arenashooter.game.gameStates.editor.editorEnum.TypeEntites;

class MainMenu implements Navigable {

	// Save & Quit variables
	private Arena arenaConstruction;
	private String fileName = "NewArena";

	private Vec2f position = new Vec2f(Editor.forVisible, 0);
	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(), setMenu = new MenuSelectionV<>(),
			saveQuitMenu = new MenuSelectionV<>();
	private MultiMenu<Prime> mainMenu = new MultiMenu<>(5, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));
	private Vec2f buttonScale = new Vec2f(15, 5);

	private Editor editor;
	
	private HashMap<Entity, Button> entityToButton = new HashMap<>();

	public MainMenu(Arena toConstruct, Editor editor) {
		this.editor = editor;

		mainMenu.setPosition(new Vec2f(Editor.forVisible, -40));
		mainMenu.addMenu(addMenu, Prime.Add);
		mainMenu.addMenu(setMenu, Prime.Set);
		mainMenu.addMenu(saveQuitMenu, Prime.Exit);

		arenaConstruction = toConstruct;

		saveQuitMenuConstruction();

		addMenuConstruction();
	}

	private void saveQuitMenuConstruction() {
		saveQuitMenu.setEcartement(8);
		Button save = new Button(0, buttonScale, "Save"), rename = new Button(0, buttonScale, "Rename File"),
				quit = new Button(0, buttonScale, "Quit");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		save.setScaleText(new Vec2f(20));
		save.setOnArm(new Trigger() {

			@Override
			public void make() {
				MapXmlWriter.writerMap(arenaConstruction, fileName);
			}
		});
		rename.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		rename.setScaleText(new Vec2f(20));
		rename.setOnArm(new Trigger() {

			@Override
			public void make() {
				// TODO
			}
		});
		quit.setOnArm(new Trigger() {

			@Override
			public void make() {
				GameMaster.gm.requestNextState(new MenuStart(), GameMaster.mapEmpty);
			}
		});
		quit.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		quit.setScaleText(new Vec2f(20));
		saveQuitMenu.addElementInListOfChoices(save, 1);
		saveQuitMenu.addElementInListOfChoices(rename, 1);
		saveQuitMenu.addElementInListOfChoices(quit, 1);
	}

	private void addMenuConstruction() {
		final float scaleText = 20f;
		for (TypeEntites type : TypeEntites.values()) {
			char first = type.name().charAt(0);
			String name = type.name().substring(1).toLowerCase();

			Button button = new Button(0, buttonScale, first + name);
			addMenu.addElementInListOfChoices(button, 1);
			button.setScaleText(new Vec2f(scaleText));
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					Entity entity = new Entity();
					String entityName = entity.genName();
					entity.attachToParent(arenaConstruction, entityName);
					Button toSetMenu = new Button(0, buttonScale, entityName);
					setMenu.addElementInListOfChoices(toSetMenu, 1);
					entityToButton.put(entity, toSetMenu);
					toSetMenu.setOnArm(new Trigger() {
						
						@Override
						public void make() {
							editor.setCurrentMenu(new EntityEditor(entity, type , MainMenu.this));
						}
					});
					toSetMenu.arm();
				}
			});
		}
		Button animation = new Button(0, buttonScale, "Animation");
		addMenu.addElementInListOfChoices(animation, 1);
		animation.setScaleText(new Vec2f(scaleText));
		animation.setOnArm(new Trigger() {

			@Override
			public void make() {
				editor.setCurrentMenu(editor.animEditor);
			}
		});

		addMenu.setEcartement(8);

	}
	
	void setButtonName(Entity entity, String name) {
		Button button = entityToButton.get(entity);
		if(button != null) {
			button.setText(name);
		}
	}

	@Override
	public boolean upAction() {
		return mainMenu.upAction();
	}

	@Override
	public boolean downAction() {
		return mainMenu.downAction();
	}

	@Override
	public boolean rightAction() {
		return mainMenu.rightAction();
	}

	@Override
	public boolean leftAction() {
		return mainMenu.leftAction();
	}

	@Override
	public boolean selectAction() {
		return mainMenu.selectAction();
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
	public void update(double delta) {
		mainMenu.update(delta);
	}

	@Override
	public void draw() {
		mainMenu.draw();
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		mainMenu.setPositionLerp(Vec2f.add(mainMenu.getPosition(), dif), lerp);
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(position, this.position);
		this.position.add(dif);
		mainMenu.setPosition(Vec2f.add(mainMenu.getPosition(), dif));
	}

	@Override
	public Vec2f getPosition() {
		return position;
	}

	@Override
	public boolean continueAction() {
		return false;
	}

}