package arenashooter.engine.ui;

import java.text.DecimalFormat;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.game.Main;

public class MenuSettings extends TabList<UiActionable> {

	private DecimalFormat df = new DecimalFormat("0.00");
	private UiActionable back = new UiActionable() {

		@Override
		public void draw() {
			// Nothing
		}
	};

	public MenuSettings(float y, Trigger onBack) {
		back.setOnArm(onBack);

		UiListVertical<UiActionable> audio = new UiListVertical<>(), video = new UiListVertical<>();
		setPosition(0, y);

		///
		/// Audio settings
		///

		UiSliderV mainVolume = new UiSliderV();
		mainVolume.value = Main.getAudioManager().getMainVolume();
		mainVolume.setTitle("Main volume");
		mainVolume.setScale(40, 8);
		mainVolume.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getAudioManager().setMainVolume((float) mainVolume.value);
			}
		});
		
		UiSliderV musicVolume = new UiSliderV();
		musicVolume.value = Main.getAudioManager().getChannelVolume(AudioChannel.MUSIC);
		musicVolume.setTitle("Music volume");
		musicVolume.setScale(40, 8);
		musicVolume.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getAudioManager().setChannelVolume(AudioChannel.MUSIC, (float) musicVolume.value);
			}
		});

		UiSliderV sfxVolume = new UiSliderV();
		sfxVolume.value = Main.getAudioManager().getChannelVolume(AudioChannel.SFX);
		sfxVolume.setTitle("Sfx volume");
		sfxVolume.setScale(40, 8);
		sfxVolume.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getAudioManager().setChannelVolume(AudioChannel.SFX, (float) sfxVolume.value);
			}
		});

		UiSliderV uiVolume = new UiSliderV();
		uiVolume.value = Main.getAudioManager().getChannelVolume(AudioChannel.UI);
		uiVolume.setTitle("UI volume");
		uiVolume.setScale(40, 8);
		uiVolume.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getAudioManager().setChannelVolume(AudioChannel.UI, (float) uiVolume.value);
			}
		});

		///
		/// Video settings
		///
		final double xScaleScH = 40 , yScaleScH = 8;
		final float ratio = 0.8f;
		ScrollerH<int[]> reso = new ScrollerH<>();
		for (int[] is : Main.getRenderer().getAvailableResolutions()) {
			reso.add(is);
			reso.changeValueView(is, is[0] + "x" + is[1]);
			if (is[0] == Main.getRenderer().getWidth() && is[1] == Main.getRenderer().getHeight()) {
				if (!reso.setValue(is))
					Main.log.warn("Couldn't set resolution button initial value");
			}
		}
		reso.setScale(xScaleScH, yScaleScH);
		reso.setLabelRatio(ratio);
		reso.setTitle("Resolution");
		reso.setOnValidation(new Trigger() {

			@Override
			public void make() {
				Main.getRenderer().resize(reso.get()[0], reso.get()[1]);
			}
		});

		ScrollerH<Boolean> fullscreen = new ScrollerH<>();
		fullscreen.add(false);
		fullscreen.changeValueView(false, "windowed");
		fullscreen.add(true);
		fullscreen.changeValueView(true, "fullscreen");
		fullscreen.setTitle("Mode");
		fullscreen.setScale(xScaleScH, yScaleScH);
		fullscreen.setLabelRatio(ratio);
		if (!fullscreen.setValue(Main.getRenderer().isFullscreen()))
			Main.log.warn("Couldn't set fullscreen button initial value");
		fullscreen.setOnValidation(new Trigger() {
			@Override
			public void make() {
				Main.getRenderer().setFullscreen(fullscreen.get().booleanValue());
			}
		});

		ScrollerH<Float> scale = new ScrollerH<>();
		scale.setScale(xScaleScH, yScaleScH);
		scale.setLabelRatio(ratio);
		for (int i = 10; i <= 40; i++) {
			Float f = Float.valueOf((float) (i * 0.05));
			scale.add(f);
			scale.changeValueView(f, df.format(f.floatValue()));
		}
		if (scale.containValue(Main.getRenderer().getResScale())) {
			if (!scale.setValue(Main.getRenderer().getResScale())) {
				Main.log.warn("Couldn't set resolution scale button initial value");
			}
		} else {
			Main.log.warn(
					"Couldn't set resolution scale button initial value because is's not on values added in button");
		}

		scale.setTitle("Resolution scale");
		scale.setOnValidation(new Trigger() {
			@Override
			public void make() {
				Main.getRenderer().setResScale(scale.get().floatValue());
			}
		});

		Button backVideo = new Button("Back");
		backVideo.setOnArm(onBack);
		backVideo.setRectangleVisible(false);
		backVideo.setScale(40 , 10);
		Button backAudio = new Button("Back");
		backAudio.setOnArm(onBack);
		backAudio.setRectangleVisible(false);
		backAudio.setScale(40 , 10);

		video.addElements(reso, fullscreen, scale, backVideo);
		audio.addElements(mainVolume, musicVolume, sfxVolume, uiVolume, backAudio);

		backVideo.addToPosition(0, 2.5);
		backAudio.addToPosition(0, 2.5);
		
		addBind("Video", video);
		addBind("Audio", audio);
		setTitleSpacing(10);
		addToScaleForeach(-4, -4 ,true);
		setSpacingForeachList(0.5);
		setArrowsDistance(15);
		setScaleArrows(5, 5);

	}

	/* Back */
	@Override
	public boolean backAction() {
		if (!getTarget().backAction()) {
			back.arm();
		}
		return true;
	}

}