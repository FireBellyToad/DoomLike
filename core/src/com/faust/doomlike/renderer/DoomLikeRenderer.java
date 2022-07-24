package com.faust.doomlike.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.DoomLikeTestGame;
import com.faust.doomlike.test.PlayerInstance;

/**
 * Renderer class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeRenderer {

    private static final int FIELD_OF_VIEW = 200;
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

        // offset bottom 2 points bt player
        final float x1 = 40 - playerInstance.getPosition().x;
        final float y1 = 10 - playerInstance.getPosition().y;
        final float x2 = 40 - playerInstance.getPosition().x;
        final float y2 = 290 - playerInstance.getPosition().y;


        // Calculate X, Y (depth) and Z (height) world position for both points
        Vector3 pointsToAdd = new Vector3();
        Vector3 otherPointsToAdd = new Vector3();

        pointsToAdd.x = x1 * playerAngleCurrentCos - y1 * playerAngleCurrentSin;
        pointsToAdd.y = y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin;
        pointsToAdd.z = 0 - playerInstance.getPosition().z;

        otherPointsToAdd.x = x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin;
        otherPointsToAdd.y = y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin;
        otherPointsToAdd.z = 0 - playerInstance.getPosition().z;

        // Calculate X and Y screen position
        pointsToAdd.x = pointsToAdd.x * FIELD_OF_VIEW / pointsToAdd.y + DoomLikeTestGame.GAME_WIDTH / 2;
        pointsToAdd.y = pointsToAdd.z * FIELD_OF_VIEW / pointsToAdd.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        otherPointsToAdd.x = otherPointsToAdd.x * FIELD_OF_VIEW / otherPointsToAdd.y + DoomLikeTestGame.GAME_WIDTH / 2;
        otherPointsToAdd.y = otherPointsToAdd.z * FIELD_OF_VIEW / otherPointsToAdd.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        if (pointsToAdd.x > 0 && pointsToAdd.x < DoomLikeTestGame.GAME_WIDTH && pointsToAdd.y > 0 && pointsToAdd.y < DoomLikeTestGame.GAME_HEIGHT)
            drawPixel(pointsToAdd.x, pointsToAdd.y);
        if (otherPointsToAdd.x > 0 && otherPointsToAdd.x < DoomLikeTestGame.GAME_WIDTH && otherPointsToAdd.y > 0 && otherPointsToAdd.y < DoomLikeTestGame.GAME_HEIGHT)
            drawPixel(otherPointsToAdd.x, otherPointsToAdd.y);

    }

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
