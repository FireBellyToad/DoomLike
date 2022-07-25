package com.faust.doomlike.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.DoomLikeTestGame;
import com.faust.doomlike.renderer.data.WallData;
import com.faust.doomlike.test.PlayerInstance;

/**
 * Renderer class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeRenderer {

    private static final int FIELD_OF_VIEW = 200;
    private static final int VERTICAL_LOOK_SCALE_FACTOR = 32;

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static final Color yellow = new Color(0xffff00ff);
    private final OrthographicCamera camera;

    public DoomLikeRenderer(SpriteBatch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    public void draw3d(PlayerInstance playerInstance) {

        this.batch.setProjectionMatrix(camera.combined);

        final float playerAngleCurrentCos = MathUtils.cosDeg(playerInstance.getAngle());
        final float playerAngleCurrentSin = MathUtils.sinDeg(playerInstance.getAngle());

        // Place the wall in world relative to player position
        final float x1 = 40 - playerInstance.getPosition().x;
        final float y1 = 10 - playerInstance.getPosition().y;
        final float x2 = 40 - playerInstance.getPosition().x;
        final float y2 = 290 - playerInstance.getPosition().y;

        //FIXME should not be temp!
        WallData tempWallData = new WallData();
        // Calculate X, Y (depth) and Z (height) world position for both points, from origin
        Vector3 pointsToAdd = tempWallData.getBottomLeftPoint();
        Vector3 otherPointsToAdd = tempWallData.getBottomRightPoint();

        pointsToAdd.x = x1 * playerAngleCurrentCos - y1 * playerAngleCurrentSin;
        pointsToAdd.y = y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin;
        // Use vertical looking angle to offset Z
        pointsToAdd.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * pointsToAdd.y) / VERTICAL_LOOK_SCALE_FACTOR);

        otherPointsToAdd.x = x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin;
        otherPointsToAdd.y = y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin;
        // Use vertical looking angle to offset Z
        otherPointsToAdd.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * otherPointsToAdd.y) / VERTICAL_LOOK_SCALE_FACTOR);

        // Calculate X and Y screen position, scaling from screen origin
        pointsToAdd.x = pointsToAdd.x * FIELD_OF_VIEW / pointsToAdd.y + DoomLikeTestGame.GAME_WIDTH / 2;
        pointsToAdd.y = pointsToAdd.z * FIELD_OF_VIEW / pointsToAdd.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        otherPointsToAdd.x = otherPointsToAdd.x * FIELD_OF_VIEW / otherPointsToAdd.y + DoomLikeTestGame.GAME_WIDTH / 2;
        otherPointsToAdd.y = otherPointsToAdd.z * FIELD_OF_VIEW / otherPointsToAdd.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        //Draw points if on screen
        if (pointsToAdd.x > 0 && pointsToAdd.x < DoomLikeTestGame.GAME_WIDTH && pointsToAdd.y > 0 && pointsToAdd.y < DoomLikeTestGame.GAME_HEIGHT)
            drawPixel(pointsToAdd.x, pointsToAdd.y);
        if (otherPointsToAdd.x > 0 && otherPointsToAdd.x < DoomLikeTestGame.GAME_WIDTH && otherPointsToAdd.y > 0 && otherPointsToAdd.y < DoomLikeTestGame.GAME_HEIGHT)
            drawPixel(otherPointsToAdd.x, otherPointsToAdd.y);

    }

    /**
     *
     * @param wallData
     */
    private void drawWall(WallData wallData){
        wallData.getBottomRightPoint();
        wallData.getBottomLeftPoint();

    }

    /**
     * Draw pixel
     *
     * @param x
     * @param y
     */
    private void drawPixel(float x, float y) {
        batch.begin();
        shapeRenderer.setColor(yellow);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.rect(x, y, 1, 1);
        shapeRenderer.end();
        batch.end();

    }

    public void dispose() {
        shapeRenderer.dispose();
    }

}
