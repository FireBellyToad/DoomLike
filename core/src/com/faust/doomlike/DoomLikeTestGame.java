package com.faust.doomlike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.DoomLikeRenderer;
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

    private SpriteBatch batch;
    private Texture img;

    private PlayerInstance playerInstance;
    private DoomLikeRenderer renderer;
    private MapWrapper testMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        playerInstance = new PlayerInstance();
        Gdx.input.setInputProcessor(playerInstance);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        renderer = new DoomLikeRenderer(batch, camera);

        //FIXME mock map
        MapData testMapData = TestMapFactory.getHollowMap();

		testMapData.getSectors().forEach(s -> {
            Gdx.app.log("DEBUG", s.getSectorUuid());
        });

        testMap = new MapWrapper(testMapData);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerInstance.doLogic();
        renderer.draw3d(testMap, playerInstance);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        img.dispose();
    }
}
