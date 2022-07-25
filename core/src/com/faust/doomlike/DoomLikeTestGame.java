package com.faust.doomlike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.faust.doomlike.renderer.DoomLikeRenderer;
import com.faust.doomlike.test.PlayerInstance;

/**
 * Test Main class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeTestGame extends ApplicationAdapter {

	public static final int GAME_WIDTH = 160;
	public static final int GAME_HEIGHT = 120;
	public static final int SCALE_FACTOR = 4;

	SpriteBatch batch;
	Texture img;

	PlayerInstance playerInstance;
	DoomLikeRenderer renderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		playerInstance = new PlayerInstance();
		Gdx.input.setInputProcessor(playerInstance);

		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

		renderer = new DoomLikeRenderer(batch, camera);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		playerInstance.doLogic();
		renderer.draw3d(playerInstance);
	}
	
	@Override
	public void dispose () {
		renderer.dispose();
		batch.dispose();
		img.dispose();
	}
}
