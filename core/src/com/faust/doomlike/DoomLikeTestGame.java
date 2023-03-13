package com.faust.doomlike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.faust.doomlike.renderer.WorldRenderer;
import com.faust.doomlike.renderer.impl.DoomLikeRenderer;
import com.faust.doomlike.utils.HeaderFormatLoader;
import com.faust.doomlike.utils.MapWrapper;

/**
 * Test Main class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeTestGame extends ApplicationAdapter {

    public static final int GAME_WIDTH = 160;
    public static final int GAME_HEIGHT = 120;
    public static final int SCALE_FACTOR = 4;

    private WorldRenderer renderer;
    private MapWrapper testMap;

    @Override
    public void create() {

        HeaderFormatLoader loader = new HeaderFormatLoader();
        testMap = new MapWrapper(loader);
        Gdx.input.setInputProcessor(testMap.getPlayerInstance());

        renderer = new DoomLikeRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        testMap.doLogic();
        renderer.drawWorld(0f,testMap);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
