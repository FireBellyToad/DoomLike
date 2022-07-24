package com.faust.doomlike.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.faust.doomlike.DoomLikeTestGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "DoomLike test";
		config.resizable = false;
		config.width = DoomLikeTestGame.GAME_WIDTH * DoomLikeTestGame.SCALE_FACTOR;
		config.height = DoomLikeTestGame.GAME_HEIGHT * DoomLikeTestGame.SCALE_FACTOR;
		new LwjglApplication(new DoomLikeTestGame(), config);
	}
}
