package arenashooter.game.gameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.ui.Imageinput;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGridWeak;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Config extends GameState {

	private GameParam gameParam;
	private UiGridWeak menu = new UiGridWeak();
	private UiImage background = new UiImage(Texture.loadTexture("data/sprites/interface/Fond Menu.png")),
			selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector_1.png"));
	private InputListener inputs = new InputListener();
	private ScrollerH<Integer> rounds;
	private UiImage
			Escape = Imageinput.ESCAPE.getImage(),
			Space = Imageinput.SPACE.getImage(),
			A = Imageinput.A.getImage(), B = Imageinput.B.getImage();
	private Label Confirm, Back;
	public Config() {
		super(1);
		gameParam = new GameParam();
		GameParam.maps.clear();
		
		ui();
		
		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent e) {
				if (e.getActionState() == ActionState.JUST_PRESSED) {
					switch (e.getAction()) {
					case UI_RIGHT:
						menu.rightAction();
						break;
					case UI_LEFT:
						menu.leftAction();
						break;
					case UI_UP:
						menu.upAction();
						break;
					case UI_DOWN:
						menu.downAction();
						break;
					case UI_OK:
						if (!menu.selectAction() && menu.getTarget().hasAction("selec")) {
							menu.getTarget().lunchAction("selec");
						}
						break;
					case UI_CONTINUE:
						if (!menu.continueAction()) {
							if (!GameParam.maps.isEmpty()) {
								Main.getGameMaster().requestNextState(new CharacterChooser(), GameMaster.mapCharChooser);
							}
						}
						break;
					case UI_BACK:
						if(!menu.backAction()) {
							Main.getGameMaster().requestPreviousState();
						}
						break;
					case UI_CANCEL:
						menu.cancelAction();
						break;
					case UI_CHANGE:
						menu.changeAction();
						break;
					default:
						break;
					}
				}
				selector.setPositionLerp(menu.getTarget().getPosition().x, menu.getTarget().getPosition().y, 32);
				if (menu.getTarget() == rounds) {
					selector.setScale(47, 12);
				} else {
					selector.setScale(12);
				}
			}
		});
	}

	private void ui() {
		background.setScale(180, 100);

		UiListVertical<UiElement> vlist = new UiListVertical<>();
		Integer[] roundsValues = { 1, 2, 3, 4, 5, 10, 15, 20, 25, -1 };
		rounds = new ScrollerH<>(roundsValues);
		rounds.setTitle("Round(s)");
		rounds.changeValueView(Integer.valueOf(-1), "\u221E");
		rounds.setBackgroundVisible(true);
		rounds.setBackgroundChange(true);
		UiImage bgU = new UiImage(0, 0, 0, 1), bgS = new UiImage(0.8, 0.8, 0.5, 1);
		rounds.setBackgroundUnselect(bgU);
		rounds.setBackgroundSelect(bgS);
		rounds.setSelectColor(0, 0, 0, 1);
		rounds.setOnValidation(new Trigger() {

			@Override
			public void make() {
				gameParam.setNbRound(rounds.get());
			}
		});
		rounds.setScale(5);
		vlist.addElement(rounds);
		vlist.setPosition(-57.5, -40);

		bgU.setScale(40, 10);
		bgS.setScale(40, 10);

		menu.addListVertical(vlist);

		selector.setScale(47,12);
		
		
		Space.setScale(Space.getScale().x / 2, Space.getScale().y / 2);
		Space.setPosition(-70, 30);

		A.setScale(A.getScale().x / 3.142, A.getScale().y / 3.142);
		A.setPosition(-65, 30);

		Confirm = new Label(" : Toggle Selection", 3);
		Confirm.setPosition(-50, 30);

		Back = new Label(" : Back", 3);
		Back.setPosition(-57.5, 38);

		Escape.setScale(Escape.getScale().x / 2, Escape.getScale().y / 2);
		Escape.setPosition(-70, 38);

		B.setScale(B.getScale().x / 3.142, B.getScale().y / 3.142);
		B.setPosition(-65, 38);
	}

	@Override
	public void init() {
		
		while(Main.loadingConfig.isAlive()) {
			// wait
		}
		
		List<File> maps = new ArrayList<>(Main.loadingConfig.getFile());
		maps.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		int i = 0, nbElemH = 7;
		double spacing = 15;
		// init
		List<UiListVertical<UiElement>> everyList = new ArrayList<>(nbElemH);
		for (int j = 0; j < nbElemH; j++) {
			UiListVertical<UiElement> newList = new UiListVertical<>();
			everyList.add(j, newList);
			newList.setPosition(j * spacing - 10, -30);
			newList.setSpacing(spacing);
			menu.addListVertical(newList);
		}

		for (File file : maps) {
			UiImage picture = new UiImage(Main.loadingConfig.getTexture(file));
			double scale = 4.5;
			picture.addToScale(-scale);
			everyList.get(i % nbElemH).addElement(picture);
			picture.addAction("selec", new Trigger() {

				@Override
				public void make() {
					double lerp = 10;
					if (GameParam.maps.contains(file.getPath())) {
						GameParam.maps.remove(file.getPath());
						picture.addToScaleLerp(-scale, lerp);
					} else {
						GameParam.maps.add(file.getPath());
						picture.addToScaleLerp(scale, lerp);
					}
				}
			});
			i++;
		}

		selector.setPosition(menu.getTarget().getPosition());

		super.init();
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		menu.update(delta);
		selector.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		// super.draw();
		Window.beginUi();
		background.draw();
		menu.draw();
		selector.draw();
		A.draw();
		B.draw();
		Back.draw();
		Confirm.draw();
		Escape.draw();
		Space.draw();
		Window.endUi();
	}

}
