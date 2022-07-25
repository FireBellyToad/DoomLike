package com.faust.doomlike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.DoomLikeRenderer;
import com.faust.doomlike.test.PlayerInstance;
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

	private final Color yellow = new Color(0xffff00ff);
	private final Color darkYellow = new Color(0xaaaa00ff);
	private final Color red = new Color(0xff0000ff);
	private final Color darkRed = new Color(0xaa0000ff);
	private final Color green = new Color(0x00ff00ff);
	private final Color darkGreen = new Color(0x00aa00ff);
	private final Color blue = new Color(0x0000ffff);
	private final Color darkBlue = new Color(0x0000aaff);



	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		playerInstance = new PlayerInstance();
		Gdx.input.setInputProcessor(playerInstance);

		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

		renderer = new DoomLikeRenderer(batch, camera);

		//FIXME mock map
		MapData testMapData = new MapData();
		testMapData.getSectors().add(new SectorData(){{
			setTopHeight(0);
			setTopHeight(40);
			setBottomColor(red);
			setTopColor(darkRed);
			this.getWalls().add(new WallData(0,0,32,0, yellow));
			this.getWalls().add(new WallData(32,0,32,32, darkYellow));
			this.getWalls().add(new WallData(32,32,0,32, yellow));
			this.getWalls().add(new WallData(0,32,0,0, darkYellow));
		}});
		testMapData.getSectors().add(new SectorData(){{
			setTopHeight(0);
			setTopHeight(40);
			setBottomColor(blue);
			setTopColor(darkBlue);
			this.getWalls().add(new WallData(64,0,96,0, red));
			this.getWalls().add(new WallData(96,0,96,32, darkRed));
			this.getWalls().add(new WallData(96,32,64,32, red));
			this.getWalls().add(new WallData(64,32,64,0, darkRed));
		}});
		testMapData.getSectors().add(new SectorData(){{
			setTopHeight(0);
			setTopHeight(40);
			setBottomColor(yellow);
			setTopColor(darkYellow);
			this.getWalls().add(new WallData(64,64,96,64, green));
			this.getWalls().add(new WallData(96,64,96,96, darkGreen));
			this.getWalls().add(new WallData(96,96,64,96, green));
			this.getWalls().add(new WallData(64,96,64,64, darkGreen));
		}});
		testMapData.getSectors().add(new SectorData(){{
			setTopHeight(0);
			setTopHeight(40);
			setBottomColor(green);
			setTopColor(darkGreen);
			this.getWalls().add(new WallData(0,64,32,64, blue));
			this.getWalls().add(new WallData(32,64,32,96, darkBlue));
			this.getWalls().add(new WallData(32,96,0,96, blue));
			this.getWalls().add(new WallData(0,96,0,64, darkBlue));
		}});

		testMapData.getSectors().forEach(s ->{
			Gdx.app.log("DEBUG", s.getSectorUuid());
		});

		testMap = new MapWrapper(testMapData);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		playerInstance.doLogic();
		renderer.draw3d(testMap, playerInstance);
	}
	
	@Override
	public void dispose () {
		renderer.dispose();
		batch.dispose();
		img.dispose();
	}
}
