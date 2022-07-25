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
        Vector3 bottomLeftPoint = tempWallData.getBottomLeftPoint();
        Vector3 bottomRightPoint = tempWallData.getBottomRightPoint();
        Vector3 topLeftPoint = tempWallData.getTopLeftPoint();
        Vector3 topRightPoint = tempWallData.getTopRightPoint();

        bottomLeftPoint.x = x1 * playerAngleCurrentCos - y1 * playerAngleCurrentSin;
        topLeftPoint.x = bottomLeftPoint.x;
        bottomLeftPoint.y = y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin;
        topLeftPoint.y = bottomLeftPoint.y;
        // Use vertical looking angle to offset Z
        bottomLeftPoint.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomLeftPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
        topLeftPoint.z = 40 + bottomLeftPoint.z;

        bottomRightPoint.x = x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin;
        topRightPoint.x = bottomRightPoint.x;
        bottomRightPoint.y = y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin;
        topRightPoint.y = bottomRightPoint.y;
        // Use vertical looking angle to offset Z
        bottomRightPoint.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomRightPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
        topRightPoint.z = 40 + bottomRightPoint.z;

        // Calculate X and Y screen position, scaling from screen origin
        bottomLeftPoint.x = bottomLeftPoint.x * FIELD_OF_VIEW / bottomLeftPoint.y + DoomLikeTestGame.GAME_WIDTH / 2;
        bottomLeftPoint.y = bottomLeftPoint.z * FIELD_OF_VIEW / bottomLeftPoint.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        bottomRightPoint.x = bottomRightPoint.x * FIELD_OF_VIEW / bottomRightPoint.y + DoomLikeTestGame.GAME_WIDTH / 2;
        bottomRightPoint.y = bottomRightPoint.z * FIELD_OF_VIEW / bottomRightPoint.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        topLeftPoint.x = topLeftPoint.x * FIELD_OF_VIEW / topLeftPoint.y + DoomLikeTestGame.GAME_WIDTH / 2;
        topLeftPoint.y = topLeftPoint.z * FIELD_OF_VIEW / topLeftPoint.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        topRightPoint.x = topRightPoint.x * FIELD_OF_VIEW / topRightPoint.y + DoomLikeTestGame.GAME_WIDTH / 2;
        topRightPoint.y = topRightPoint.z * FIELD_OF_VIEW / topRightPoint.y + DoomLikeTestGame.GAME_HEIGHT / 2;

        //Draw points if on screen
        drawWall(tempWallData);

    }


    /**
     * draw a line of vert
     *
     * @param wallData
     */
    private void drawWall(WallData wallData) {

        int bottomPointYDistance = (int) (wallData.getBottomRightPoint().y - wallData.getBottomLeftPoint().y);
        int xDistance = (int) Math.max(1, (wallData.getBottomRightPoint().x - wallData.getBottomLeftPoint().x));
        int topPointYDistance = (int) (wallData.getTopRightPoint().y - wallData.getTopLeftPoint().y);
        int xStartingPosition = (int) wallData.getBottomLeftPoint().x;

        //Draw x vertical lines
        for (int xToRender = (int) wallData.getBottomLeftPoint().x; xToRender < wallData.getBottomRightPoint().x; xToRender++) {
            // Get the Y start and end point (0.5 is used for rounding)
            int yBottomPoint = (int) (bottomPointYDistance * (xToRender - xStartingPosition + 0.5) / xDistance + wallData.getBottomLeftPoint().y);
            int yTopPoint = (int) (topPointYDistance * (xToRender - xStartingPosition + 0.5) / xDistance + wallData.getTopLeftPoint().y);

            drawPixel(xToRender, yBottomPoint, yellow);
            drawPixel(xToRender, yTopPoint, yellow);

            // draw vertical line to fill the wall
            for (int yToRender = yBottomPoint; yToRender < yTopPoint; yToRender++) {
                drawPixel(xToRender, yToRender, yellow);

            }
            // draw vertical line to fill the wall
//            drawLine(xToRender, yBottomPoint, xToRender, yTopPoint, yellow);

        }
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param pixelColor
     */
    private void drawLine(float x1, float y1,float x2, float y2, Color pixelColor) {
        batch.begin();
        shapeRenderer.setColor(pixelColor);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.rectLine(x1,y1,x2,y2,1);
        shapeRenderer.end();
        batch.end();

    }

    /**
     * Draw pixel
     *
     * @param x
     * @param y
     * @param pixelColor
     */
    private void drawPixel(float x, float y, Color pixelColor) {
        batch.begin();
        shapeRenderer.setColor(pixelColor);
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
