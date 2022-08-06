package com.faust.doomlike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.renderer.WorldRenderer;
import com.faust.doomlike.renderer.impl.DoomLikeRenderer;
import com.faust.doomlike.renderer.impl.True3DRenderer;
import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.test.TestMapFactory;
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

    private PlayerInstance playerInstance;
    private WorldRenderer renderer;
    private MapWrapper testMap;

    @Override
    public void create() {

        playerInstance = new PlayerInstance();
        Gdx.input.setInputProcessor(playerInstance);


        //FIXME mock map
        MapData testMapData = TestMapFactory.getHollowMap();

		testMapData.getSectors().forEach(s -> {
            Gdx.app.log("DEBUG", s.getSectorUuid());
        });

        testMap = new MapWrapper(testMapData);
//        renderer = new DoomLikeRenderer();
        renderer = new True3DRenderer(testMap);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerInstance.doLogic();
        renderer.drawWorld(testMap, playerInstance);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
