package com.faust.doomlike.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.DoomLikeTestGame;
import com.faust.doomlike.renderer.data.WallData;
import com.faust.doomlike.test.PlayerInstance;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Renderer class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeRenderer {

    private static final float FIELD_OF_VIEW = 200;
    private static final float VERTICAL_LOOK_SCALE_FACTOR = 32;

    private final SpriteBatch batch;
    private final ShapeDrawer shapeDrawer;
    private static final Color yellow = new Color(0xffff00ff);
    private final OrthographicCamera camera;
    private final Texture shapeRendererTexture;

    public DoomLikeRenderer(SpriteBatch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        shapeRendererTexture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();

        this.shapeDrawer = new ShapeDrawer(batch,new TextureRegion(shapeRendererTexture, 0, 0, 1, 1));
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
        //Must be not 0
        bottomLeftPoint.y = Math.max(1,y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin);
        topLeftPoint.y = bottomLeftPoint.y;
        // Use vertical looking angle to offset Z
        bottomLeftPoint.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomLeftPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
        topLeftPoint.z = 40 + bottomLeftPoint.z;

        bottomRightPoint.x = x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin;
        topRightPoint.x = bottomRightPoint.x;
        //Must be not 0
        bottomRightPoint.y = Math.max(1,y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin);
        topRightPoint.y = bottomRightPoint.y;
        // Use vertical looking angle to offset Z
        bottomRightPoint.z = 0 - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomRightPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
        topRightPoint.z = 40 + bottomRightPoint.z;

        // If wall is behind player
        if(bottomLeftPoint.y < 0 && bottomRightPoint.y <1)
            return;
        else if(bottomLeftPoint.y < 0){
            clipBehindPlayer(bottomLeftPoint, bottomRightPoint);
            clipBehindPlayer(topLeftPoint, topRightPoint);

        } else if(bottomRightPoint.y < 0) {
            clipBehindPlayer(bottomRightPoint, bottomLeftPoint);
            clipBehindPlayer(topRightPoint, topLeftPoint);
        }


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

        Vector3 bottomLeftPoint = wallData.getBottomLeftPoint();
        Vector3 bottomRightPoint = wallData.getBottomRightPoint();

        float bottomPointYDistance = (bottomRightPoint.y - bottomLeftPoint.y);
        float xDistance = Math.max(1, (bottomRightPoint.x - bottomLeftPoint.x));
        float topPointYDistance =  (wallData.getTopRightPoint().y - wallData.getTopLeftPoint().y);
        float xStartingPosition =  bottomLeftPoint.x;

        //Clip x
        bottomLeftPoint.x = MathUtils.clamp(bottomLeftPoint.x, 1, DoomLikeTestGame.GAME_WIDTH-1);
        bottomRightPoint.x = MathUtils.clamp(bottomRightPoint.x, 1, DoomLikeTestGame.GAME_WIDTH-1);

        batch.begin();
        //Draw x vertical lines
        for (float xToRender = bottomLeftPoint.x; xToRender < bottomRightPoint.x; xToRender++) {
            // Get the Y start and end point (0.5 is used for rounding)
            float yBottomPoint = (float) (bottomPointYDistance * (xToRender - xStartingPosition + 0.5) / xDistance + bottomLeftPoint.y);
            float yTopPoint = (float) (topPointYDistance * (xToRender - xStartingPosition + 0.5) / xDistance + wallData.getTopLeftPoint().y);

            //Clip Y
            yBottomPoint = MathUtils.clamp(yBottomPoint, 1, DoomLikeTestGame.GAME_HEIGHT-1);
            yTopPoint = MathUtils.clamp(yTopPoint, 1, DoomLikeTestGame.GAME_HEIGHT-2);

//            // Draw pixel by pixel (HEAVY)
            drawPixel(xToRender, yBottomPoint, yellow);
            drawPixel(xToRender, yTopPoint, yellow);

            // draw vertical line to fill the wall
            for (float yToRender = yBottomPoint; yToRender < yTopPoint; yToRender++) {
                drawPixel(xToRender, yToRender, yellow);

            }

            // draw vertical line to fill the wall
//            drawLine(xToRender, yBottomPoint, xToRender, yTopPoint, yellow);

        }

        batch.end();
    }

    /**
     *
     * @param pointA
     * @param pointB
     */
    private void clipBehindPlayer(Vector3 pointA, Vector3 pointB) {

        float distanceFromAToB = Math.max(1, pointA.y-pointB.y);
        float intersectionFactor = pointA.y / distanceFromAToB;

        pointA.x = pointA.x + intersectionFactor*(pointB.x-pointA.x);
        pointA.y = Math.max(1, pointB.y + intersectionFactor*(pointB.y-pointA.y));
        pointA.z = pointB.z + intersectionFactor*(pointB.z-pointB.z);

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
        shapeDrawer.setColor(pixelColor);
        shapeDrawer.line(x1,y1,x2,y2,1);
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
        shapeDrawer.setColor(pixelColor);
        shapeDrawer.rectangle(x, y, 1, 1);

    }

    public void dispose() {
        shapeRendererTexture.dispose();
    }

}
