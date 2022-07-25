package com.faust.doomlike.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.DoomLikeTestGame;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.utils.MapWrapper;
import com.faust.doomlike.utils.SectorWrapper;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Renderer class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeRenderer {

    private static final int FIELD_OF_VIEW = 200;
    private static final int VERTICAL_LOOK_SCALE_FACTOR = 32;

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

        this.shapeDrawer = new ShapeDrawer(batch, new TextureRegion(shapeRendererTexture, 0, 0, 1, 1));
    }

    public void draw3d(MapWrapper map, PlayerInstance playerInstance) {

        this.batch.setProjectionMatrix(camera.combined);

        //For eachSectors, render all walls
        map.getSectors().sort((s1, s2) -> Float.compare(s2.getDepth(), s1.getDepth()));

        for (SectorWrapper sector : map.getSectors()) {
            //Clear distance
            sector.setDepth(0);
            sector.getSurfaceYforXMap().clear();

            if (playerInstance.getPosition().z < sector.getBottomHeight()) {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.BOTTOM);
            } else if (playerInstance.getPosition().z > sector.getTopHeight()) {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.TOP);
            } else {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.NONE);
            }

            drawAllSectorWalls(sector, playerInstance, true);
            drawAllSectorWalls(sector, playerInstance, false);
            //Find average sector distance
            sector.setDepth(sector.getDepth() / sector.getWalls().size());
        }
    }

    private void drawAllSectorWalls(SectorWrapper sector, PlayerInstance playerInstance, boolean backfaceCulling) {

        final float playerAngleCurrentCos = MathUtils.cosDeg(playerInstance.getAngle());
        final float playerAngleCurrentSin = MathUtils.sinDeg(playerInstance.getAngle());

        // helper variables
        final Vector3 bottomLeftPoint = new Vector3();
        final Vector3 bottomRightPoint = new Vector3();
        final Vector3 topLeftPoint = new Vector3();
        final Vector3 topRightPoint = new Vector3();

        for (WallData wall : sector.getWalls()) {

            // Place the wall in world relative to player position
            int x1 = (int) (wall.getBottomLeftPoint().x - playerInstance.getPosition().x);
            int y1 = (int) (wall.getBottomLeftPoint().y - playerInstance.getPosition().y);
            int x2 = (int) (wall.getBottomRightPoint().x - playerInstance.getPosition().x);
            int y2 = (int) (wall.getBottomRightPoint().y - playerInstance.getPosition().y);

            //Swap for drawing backface
            if (backfaceCulling) {
                int tempSwap = x1;
                x1 = x2;
                x2 = tempSwap;
                tempSwap = y1;
                y1 = y2;
                y2 = tempSwap;
            }

            // Calculate X, Y (depth) and Z (height) world position for both points, from origin
            bottomLeftPoint.x = x1 * playerAngleCurrentCos - y1 * playerAngleCurrentSin;
            topLeftPoint.x = bottomLeftPoint.x;
            //Must be not 0
            bottomLeftPoint.y = Math.max(1, y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin);
            topLeftPoint.y = bottomLeftPoint.y;
            // Use vertical looking angle to offset Z
            bottomLeftPoint.z = sector.getBottomHeight() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomLeftPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
            topLeftPoint.z = sector.getTopHeight() + bottomLeftPoint.z;

            bottomRightPoint.x = x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin;
            topRightPoint.x = bottomRightPoint.x;
            //Must be not 0
            bottomRightPoint.y = Math.max(1, y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin);
            topRightPoint.y = bottomRightPoint.y;
            // Use vertical looking angle to offset Z
            bottomRightPoint.z = sector.getBottomHeight() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomRightPoint.y) / VERTICAL_LOOK_SCALE_FACTOR);
            topRightPoint.z = sector.getTopHeight() + bottomRightPoint.z;

            // Store this wall distance
            sector.setDepth(Vector2.Zero.dst((bottomLeftPoint.x + bottomRightPoint.x) / 2, (bottomLeftPoint.y + bottomRightPoint.y) / 2));

            // If wall is behind player
            if (bottomLeftPoint.y < 0 && bottomRightPoint.y < 1)
                //don't draw
                continue;
            else if (bottomLeftPoint.y < 0) {
                clipBehindPlayer(bottomLeftPoint, bottomRightPoint);
                clipBehindPlayer(topLeftPoint, topRightPoint);

            } else if (bottomRightPoint.y < 0) {
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
            float bottomPointYDistance = (bottomRightPoint.y - bottomLeftPoint.y);
            float xDistance = Math.max(1, (bottomRightPoint.x - bottomLeftPoint.x));
            float topPointYDistance = (topRightPoint.y - topLeftPoint.y);
            float xStartingPosition =  bottomLeftPoint.x;

            //Clip x
            bottomLeftPoint.x = MathUtils.clamp(bottomLeftPoint.x, 1, DoomLikeTestGame.GAME_WIDTH);
            bottomRightPoint.x = MathUtils.clamp(bottomRightPoint.x, 1, DoomLikeTestGame.GAME_WIDTH);

            batch.begin();
            //Draw x vertical lines
            for (float xToRender = bottomLeftPoint.x; xToRender < bottomRightPoint.x; xToRender++) {
                // Get the Y start and end point (0.5 is used for rounding)
                float yBottomPoint =  (bottomPointYDistance * (xToRender - xStartingPosition + 0.5f) / xDistance + bottomLeftPoint.y);
                float yTopPoint =  (topPointYDistance * (xToRender - xStartingPosition + 0.5f) / xDistance + topLeftPoint.y);

                //Clip Y
                yBottomPoint = MathUtils.clamp(yBottomPoint, 1, DoomLikeTestGame.GAME_HEIGHT);
                yTopPoint = MathUtils.clamp(yTopPoint, 1, DoomLikeTestGame.GAME_HEIGHT);


                int approx = MathUtils.floor(xToRender);
                //Store surface information
                if (backfaceCulling) {
                    if (SectorWrapper.SurfaceShownEnum.BOTTOM.equals(sector.getSurfaceToShow())) {
                        sector.getSurfaceYforXMap().put(approx, yBottomPoint);
                        continue;
                    }
                    if (SectorWrapper.SurfaceShownEnum.TOP.equals(sector.getSurfaceToShow())) {
                        sector.getSurfaceYforXMap().put(approx, yTopPoint);
                        continue;
                    }
                } else if (sector.getSurfaceYforXMap().containsKey(approx)) {
                    if (SectorWrapper.SurfaceShownEnum.BOTTOM.equals(sector.getSurfaceToShow())) {
                        drawLine(xToRender, sector.getSurfaceYforXMap().get(approx), xToRender, yTopPoint, sector.getBottomColor());
                    }
                    if (SectorWrapper.SurfaceShownEnum.TOP.equals(sector.getSurfaceToShow())) {
                        drawLine(xToRender, yBottomPoint, xToRender, sector.getSurfaceYforXMap().get(approx), sector.getTopColor());
                    }
                }

//                // draw vertical line to fill the wall pixel by pixel
//                for (int yToRender = yBottomPoint; yToRender < yTopPoint; yToRender++) {
//                    drawPixel(xToRender, yToRender, wall.getColor());
//                }

                // draw vertical line to fill the wall
                drawLine(xToRender,  yBottomPoint, xToRender,  yTopPoint, wall.getColor());

            }

            batch.end();

        }

    }

    /**
     * @param pointA
     * @param pointB
     */
    private void clipBehindPlayer(Vector3 pointA, Vector3 pointB) {

        float distanceFromAToB = Math.max(1, pointA.y - pointB.y);
        float intersectionFactor = (pointA.y / distanceFromAToB);

        pointA.x = pointA.x + intersectionFactor * (pointB.x - pointA.x);
        pointA.y = Math.max(1, pointB.y + intersectionFactor * (pointB.y - pointA.y));
        pointA.z = pointB.z + intersectionFactor * (pointB.z - pointB.z);

    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param pixelColor
     */
    private void drawLine(float x1, float y1, float x2, float y2, Color pixelColor) {
        shapeDrawer.setColor(pixelColor);
        shapeDrawer.line(x1, y1, x2, y2, 1);

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
