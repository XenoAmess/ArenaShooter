package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class CharacterChooser extends GameState {

	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	private HashMap<Controller, CharacterSprite> sprites = new HashMap<>(1);
	final int posdedepart = -300;
	private int i = posdedepart;

	// private boolean suppr;

	public Collection<Controller> getControllers() {
		return controllers.values();
	}

	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your failleterre");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");

		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, "Q or D to change your figther");
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, -400, -10), new Vec3f(250), text2);
		textEnt2.attachToParent(map, "Text_char");

		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, "Z and S to change your skin");
		TextSpatial textEnt3 = new TextSpatial(new Vec3f(0, -350, -10), new Vec3f(250), text3);
		textEnt3.attachToParent(map, "Text_touch");

		Text text4 = new Text(Main.font, Text.TextAlignH.CENTER, "Press ENTER to go to the map chooser");
		TextSpatial textEnt4 = new TextSpatial(new Vec3f(0, 400, -10), new Vec3f(350), text4);
		textEnt4.attachToParent(map, "Text_touch2");

		Controller controllerKeyboard = new Controller(Device.KEYBOARD);
		controllers.put(Device.KEYBOARD, controllerKeyboard);
		CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), controllerKeyboard.getCharInfo());
		sprites.put(controllerKeyboard, c);
		i += 150;
		c.attachToParent(map, c.genName());
	}

	@Override
	public void update(double delta) {
		super.update(delta);

		for (Device device : Device.values()) {
			if (Input.actionPressed(device, Action.UI_OK) && !controllers.keySet().contains(device)) {
				Controller newController = new Controller(device);
				controllers.put(device, newController);
				CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), newController.getCharInfo());
				sprites.put(newController, c);
				c.attachToParent(map, c.genName());
				i += 150;
			}
			// TODO : remove controller when UI_BACK is pressed
			if (Input.actionPressed(device, Action.UI_BACK) && controllers.keySet().contains(device)
					&& !device.equals(device.KEYBOARD)) {

				CharacterSprite sp = sprites.get(controllers.get(device));
				
				
				sprites.get((controllers.get(device))).detach();
				sprites.remove((controllers.get(device)));
				controllers.remove(device);
				//i -= 150;
				
				// replacement des persos après suppr
				for (Map.Entry<Controller, CharacterSprite> entry : sprites.entrySet()) {
					Controller key = entry.getKey();
					CharacterSprite value = entry.getValue();
					//int j = i;
					float jj = value.position.x;
					if (!key.getDevice().equals(device.KEYBOARD)) {
						
						if (jj > sp.position.x) {
							jj -= 150;
							value.destroy();
							Vec2f pos = new Vec2f(jj, 0);
							//sprites.remove(key);
							CharacterSprite c = new CharacterSprite(pos, key.getCharInfo());
							sprites.put(key, c);
							c.attachToParent(map, c.genName());
						}
					}
				}
				i -= 150;

			}

		}

		// Update controllers
		for (Controller controller : controllers.values()) {
			controller.step(delta);

			// Temp sprite changing
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_RIGHT)) {
				controller.getCharInfo().classNext();
				Vec2f pos = sprites.get(controller).position;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_LEFT)) {
				controller.getCharInfo().classPrev();
				Vec2f pos = sprites.get(controller).position;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
				controller.getCharInfo().skinNext();
				Vec2f pos = sprites.get(controller).position;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
				controller.getCharInfo().skinPrev();
				Vec2f pos = sprites.get(controller).position;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			}
		}

		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState();
		} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)) {
			GameMaster.gm.requestPreviousState();
		}

		map.step(delta);
	}

}
///*
//
//
//
//
//
//
//
//
//
//
//
//
//package arenashooter.game.gameStates;
//
//import java.lang.reflect.Array;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//
//import arenashooter.engine.graphics.PostProcess;
//import arenashooter.engine.graphics.Window;
//import arenashooter.engine.graphics.fonts.Text;
//import arenashooter.engine.input.Device;
//import arenashooter.engine.input.Input;
//import arenashooter.engine.input.Action;
//import arenashooter.engine.math.Vec2f;
//import arenashooter.engine.math.Vec3f;
//import arenashooter.entities.Controller;
//import arenashooter.entities.spatials.CharacterSprite;
//import arenashooter.entities.spatials.TextSpatial;
//import arenashooter.game.GameMaster;
//import arenashooter.game.Main;
//
//public class CharacterChooser extends GameState {
//
//	private HashMap<Device, Controller> controllers = new HashMap<>(1);
//	private HashMap<Controller, CharacterSprite> sprites = new HashMap<>(1);
//
//	private int i = -300;
//	// tableau pour gérer le placement des characters sur l'ecran
//	private LinkedList<CharacterSprite> placement = new LinkedList<CharacterSprite>();
//
//	public Collection<Controller> getControllers() {
//		return controllers.values();
//	}
//
//	@Override
//	public void init() {
//		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
//
//		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your failleterre");
//		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
//		textEnt.attachToParent(map, "Text_Select");
//
//		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, "Q or D to change your figther");
//		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, -400, -10), new Vec3f(250), text2);
//		textEnt2.attachToParent(map, "Text_char");
//
//		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, "Z and S to change your skin");
//		TextSpatial textEnt3 = new TextSpatial(new Vec3f(0, -350, -10), new Vec3f(250), text3);
//		textEnt3.attachToParent(map, "Text_touch");
//
//		Text text4 = new Text(Main.font, Text.TextAlignH.CENTER, "Press ENTER to go to the map chooser");
//		TextSpatial textEnt4 = new TextSpatial(new Vec3f(0, 400, -10), new Vec3f(350), text4);
//		textEnt4.attachToParent(map, "Text_touch2");
//
//		Controller controllerKeyboard = new Controller(Device.KEYBOARD);
//		controllers.put(Device.KEYBOARD, controllerKeyboard);
//		CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), controllerKeyboard.getCharInfo());
//		sprites.put(controllerKeyboard, c);
//		i += 150;
//		c.attachToParent(map, c.genName());
//	}
//
//	@Override
//	public void update(double delta) {
//		//Add controller
//		for (Device device : Device.values()) {
//			if (Input.actionPressed(device, Action.UI_OK) && !controllers.keySet().contains(device)) {
//				Controller newController = new Controller(device);
//				controllers.put(device, newController);
//				CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), newController.getCharInfo());
//				sprites.put(newController, c);
//				c.attachToParent(map, c.genName());
//				i += 150;
//				placement.add(c);
//			}
//			
//			
//			
//		}
//
//		// Update controllers
//		for ( Controller controller : controllers.values() ) {
//			controller.step(delta);
//			
//			// TODO : remove controller when UI_BACK is pressed
//			if (Input.actionJustPressed(controller.getDevice(), Action.UI_BACK) && !controller.getDevice().equals(Device.KEYBOARD)) {
//				boolean boo = false;
//				for (CharacterSprite characterSprite : placement) {
//					if (sprites.get(controller) != null/* && sprites.get(controller).equals(characterSprite)*/) {
//						boo = true;
//						System.out.println("blepdfghjk");
//					}
//					if (boo) {
//						 //characterSprite.localPosition.x -= 150;
//						placement.remove(sprites.get(controller));
//						sprites.get(controller).detach();
//						sprites.remove(controller);
//						i -= 150;
//						controllers.remove(controller.getDevice());
//						System.out.println(" "+i+" ");
//						characterSprite.position.x -=150;
//						boo=false;
//					}
//				}
////				if(boo) {
//	//				break;
//		//		}
////				placement.remove(sprites.get(controller));
////				sprites.get(controller).detach();
////				sprites.remove(controller);
////				i -= 150;
////				controllers.remove(controller.getDevice());
//			}
//			
//			// Temp sprite changing blep
//			if (Input.actionJustPressed(controller.getDevice(), Action.UI_RIGHT)) {
//				controller.getCharInfo().classNext();
//				Vec2f pos = sprites.get(controller).pos();
//				sprites.get(controller).detach();
//				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
//				sprites.put(controller, c);
//				c.attachToParent(map, c.genName());
//			
//			
//			
//				/*if (Input.actionPressed(controller.getDevice(), Action.UI_BACK) && !controller.getDevice().equals(Device.KEYBOARD)) {
//					boolean boo = false;
//					for (CharacterSprite characterSprite : placement) {
//						if (sprites.get(controller) != null /*&& sprites.get(controller).equals(characterSprite)*///) {
//				/*			boo = true;
//							System.out.println("blepdfghjk");
//						}
//						if (boo) {
//							 //characterSprite.localPosition.x -= 150;
//							placement.remove(sprites.get(controller));
//							sprites.get(controller).detach();
//							sprites.remove(controller);
//							i -= 150;
//							controllers.remove(controller.getDevice());
//							System.out.println(" "+i+" ");
//							characterSprite.position.x -=150;
//							boo=false;
//						}
//					}*/
//			//} 
//
//			
//			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_LEFT)) {
//				controller.getCharInfo().classPrev();
//				Vec2f pos = sprites.get(controller).pos();
//				sprites.get(controller).detach();
//				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
//				sprites.put(controller, c);
//				c.attachToParent(map, c.genName());
//			
//			/*
//			
//			
//				if (Input.actionPressed(controller.getDevice(), Action.UI_BACK) && !controller.getDevice().equals(Device.KEYBOARD)) {
//					boolean boo = false;
//					for (CharacterSprite characterSprite : placement) {
//						if (sprites.get(controller) != null /*&& sprites.get(controller).equals(characterSprite)*///) {
//				/*			boo = true;
//							System.out.println("blepdfghjk");
//						}
//						if (boo) {
//							 //characterSprite.localPosition.x -= 150;
//							placement.remove(sprites.get(controller));
//							sprites.get(controller).detach();
//							sprites.remove(controller);
//							i -= 150;
//							controllers.remove(controller.getDevice());
//							System.out.println(" "+i+" ");
//							characterSprite.position.x -=150;
//							boo=false;
//						}
//					}
//				}*/
//				
//			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
//				controller.getCharInfo().skinNext();
//				Vec2f pos = sprites.get(controller).pos();
//				sprites.get(controller).detach();
//				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
//				sprites.put(controller, c);
//				c.attachToParent(map, c.genName());
//			
//			/*
//			
//				if (Input.actionPressed(controller.getDevice(), Action.UI_BACK) && !controller.getDevice().equals(Device.KEYBOARD)) {
//					boolean boo = false;
//					for (CharacterSprite characterSprite : placement) {
//						if (sprites.get(controller) != null/* && sprites.get(controller).equals(characterSprite)*///) {
//				/*			boo = true;
//							System.out.println("blepdfghjk");
//						}
//						if (boo) {
//							 //characterSprite.localPosition.x -= 150;
//							placement.remove(sprites.get(controller));
//							sprites.get(controller).detach();
//							sprites.remove(controller);
//							i -= 150;
//							controllers.remove(controller.getDevice());
//							System.out.println(" "+i+" ");
//							characterSprite.position.x -=150;
//							boo=false;
//						}
//					}
//
//				}*/
//			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
//				controller.getCharInfo().skinPrev();
//				Vec2f pos = sprites.get(controller).pos();
//				sprites.get(controller).detach();
//				CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
//				sprites.put(controller, c);
//				c.attachToParent(map, c.genName());
//			
//				/*
//				if (Input.actionPressed(controller.getDevice(), Action.UI_BACK) && !controller.getDevice().equals(Device.KEYBOARD)) {
//					boolean boo = false;
//					for (CharacterSprite characterSprite : placement) {
//						if (sprites.get(controller) != null /*&& sprites.get(controller).equals(characterSprite)*///) {
//					/*		boo = true;
//							System.out.println("blepdfghjk");
//						}
//						if (boo) {
//							 //characterSprite.localPosition.x -= 150;
//							placement.remove(sprites.get(controller));
//							sprites.get(controller).detach();
//							sprites.remove(controller);
//							i -= 150;
//							controllers.remove(controller.getDevice());
//							System.out.println(" "+i+" ");
//							characterSprite.position.x -=150;
//							boo=false;
//						}
//					}}
//
//			}*/
//		}
//
//		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
//			GameMaster.gm.requestNextState();
//		} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)) {
//			GameMaster.gm.requestPreviousState();
//		}
//
//		map.step(delta);
//	}
////	placement.remove(sprites.get(controller));
////	sprites.get(controller).detach();
////	sprites.remove(controller);
////	i -= 150;
////	controllers.remove(controller.getDevice());
//
//}}
//
//
//
//
//
//
//
//
//
//
//*/
