package net.davidbrowne.punchleague;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.davidbrowne.punchleague.Screens.IntroScreen;
import net.davidbrowne.punchleague.Tools.AdHandler;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	public static final int PPM =100;
	public static final int V_WIDTH = 1600;
	public static final int V_HEIGHT = 900;
	public static final short FIGHTER_BIT =1;
	public static final short PLAYER_BIT = 2;
	public static final short GROUND_BIT=4;
	public static final short PUNCH_BIT=8;
	private float volume=1;
	private float music_volume=1;
	public AssetManager manager;
	private Music music;

	public Game() {
	}

	AdHandler handler;
	boolean toggle = true;

	public Game(AdHandler handler) {
		this.handler = handler;
		handler.showAds(true);
	}
	@Override
	public void create () {
		manager = new AssetManager();
		batch = new SpriteBatch();
		manager.load("music/music1.wav", Music.class);
		manager.load("sounds/punch.wav", Sound.class);
		manager.load("sounds/bell.wav", Sound.class);
		manager.finishLoading();
		music = manager.get("music/music1.wav",Music.class);
		music.setVolume(music_volume);
		music.setLooping(true);
		setScreen(new IntroScreen(this,manager));

	}

	public Music getMusic() {
		return music;
	}

	public float getVolume() {
		return volume;
	}

	public void setMusic_volume(float music_volume) {
		this.music_volume = music_volume;
	}

	public float getMusic_volume() {
		return music_volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public void render () {
		super.render();
		Gdx.app.log("FPS: "," "+Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
