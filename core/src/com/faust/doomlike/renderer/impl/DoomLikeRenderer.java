package com.faust.doomlike.renderer.impl;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.DoomLikeTestGame;
import com.faust.doomlike.data.DoomLikeTextureData;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.WorldRenderer;
import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.utils.DoomLikeTextureWrapper;
import com.faust.doomlike.utils.MapWrapper;
import com.faust.doomlike.utils.SectorWrapper;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Renderer class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeRenderer implements WorldRenderer<MapWrapper> {

    private static final float FIELD_OF_VIEW = 200f;
    private static final float VERTICAL_LOOK_SCALE_FACTOR = 32f;
    public static final float RBG_CONVERSION_FACTOR = 255f;
    private static final float SHADE_REDUCE_FACTOR = 2f;
    private static final float X_OFFSET = DoomLikeTestGame.GAME_WIDTH / 2f;
    private static final float Y_OFFSET = DoomLikeTestGame.GAME_HEIGHT / 2f;
    //FIXME multiplicating for Player speed doesn't work for a speed not equal to 2.5!!
    private static final float PLAYER_OFFSET = 24 * (PlayerInstance.SPEED); //24 is a magic number found through trial and error
    private static final float LOOK_UP_DOWN_FLOOR_FACTOR = (float) (Math.PI * 2);
    private static final float TILE_FACTOR = 7f;

    private final SpriteBatch batch;
    private final ShapeDrawer shapeDrawer;
    private final OrthographicCamera camera;
    private final Texture shapeRendererTexture;

    public DoomLikeRenderer() {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, DoomLikeTestGame.GAME_WIDTH, DoomLikeTestGame.GAME_HEIGHT);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        shapeRendererTexture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();

        this.shapeDrawer = new ShapeDrawer(batch, new TextureRegion(shapeRendererTexture, 0, 0, 1, 1));
    }

    public void drawWorld(float stateTime, MapWrapper map) {
        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);

        final PlayerInstance playerInstance = map.getPlayerInstance();

        //For eachSectors, render all walls
        map.getSectors().sort((s1, s2) -> Float.compare(s2.getDepth(), s1.getDepth()));

        batch.begin();
        for (SectorWrapper sector : map.getSectors()) {
            //Clear distance
            sector.setDepth(0);
            sector.getSurfaceYforXMap().clear();

            //Check which surface must be rendered
            if (playerInstance.getPosition().z < sector.getBottomZ()) {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.BOTTOM);
            } else if (playerInstance.getPosition().z > sector.getTopZ()) {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.TOP);
            } else {
                sector.setSurfaceToShow(SectorWrapper.SurfaceShownEnum.NONE);
            }

            drawAllSectorWalls(sector, playerInstance, true);
            drawAllSectorWalls(sector, playerInstance, false);
            //Find average sector distance
            sector.setDepth(Math.round(sector.getDepth() / sector.getWalls().size()));
        }

        this.shapeDrawer.update();
        batch.end();
    }

    private void drawAllSectorWalls(SectorWrapper sector, PlayerInstance playerInstance, boolean backfaceCulling) {

        //Texture Wrapper and data placeholders
        DoomLikeTextureWrapper textureWrapper;
        DoomLikeTextureData textureData;

        //index for pixel Color
        int pixelIndex;

        final float playerAngleCurrentCos = MathUtils.cosDeg(playerInstance.getAngle());
        final float playerAngleCurrentSin = MathUtils.sinDeg(playerInstance.getAngle());

        // helper variables
        final Vector3 bottomLeftPoint = new Vector3(); // wx[0]  wy[0]  wz[0]
        final Vector3 bottomRightPoint = new Vector3(); // wx[1]  wy[1]  wz[1]
        final Vector3 topLeftPoint = new Vector3(); // wx[2]  wy[2]  wz[2]
        final Vector3 topRightPoint = new Vector3(); // wx[3]  wy[3]  wz[3]

        for (WallData wall : sector.getWalls()) {


            // Place the wall in world relative to player position
            float x1 = (wall.getBottomLeftPoint().x - MathUtils.round(playerInstance.getPosition().x));
            float y1 = (wall.getBottomLeftPoint().y - MathUtils.round(playerInstance.getPosition().y));
            float x2 = (wall.getBottomRightPoint().x - MathUtils.round(playerInstance.getPosition().x));
            float y2 = (wall.getBottomRightPoint().y - MathUtils.round(playerInstance.getPosition().y));

            //Swap for drawing backface
            if (backfaceCulling) {
                float tempSwap = x1;
                x1 = x2;
                x2 = tempSwap;
                tempSwap = y1;
                y1 = y2;
                y2 = tempSwap;
            }

            // Calculate X, Y (depth) and Z (height) world position for both points, from origin
            bottomLeftPoint.x = Math.round(x1 * playerAngleCurrentCos - y1 * playerAngleCurrentSin);
            topLeftPoint.x = bottomLeftPoint.x;

            //Must be not 0
            bottomLeftPoint.y = Math.round(Math.max(1, y1 * playerAngleCurrentCos + x1 * playerAngleCurrentSin));
            topLeftPoint.y = bottomLeftPoint.y;

            // Use vertical looking angle to offset Z (wz[0]=S[s].z1-P.z+((P.l*wy[0])/32.0);)
            bottomLeftPoint.z = Math.round(sector.getBottomZ() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomLeftPoint.y) / VERTICAL_LOOK_SCALE_FACTOR));
            topLeftPoint.z = Math.round(sector.getTopZ() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomLeftPoint.y) / VERTICAL_LOOK_SCALE_FACTOR));

            bottomRightPoint.x = Math.round(x2 * playerAngleCurrentCos - y2 * playerAngleCurrentSin);
            topRightPoint.x = bottomRightPoint.x;
            //Must be not 0
            bottomRightPoint.y = Math.round(Math.max(1, y2 * playerAngleCurrentCos + x2 * playerAngleCurrentSin));
            topRightPoint.y = bottomRightPoint.y;

            // add this wall distance to sector depth
            sector.addToDepth(Math.round(Vector2.Zero.dst((bottomLeftPoint.x + bottomRightPoint.x) / 2, (bottomLeftPoint.y + bottomRightPoint.y) / 2)));

            // Use vertical looking angle to offset Z ( wz[1]=S[s].z1-P.z+((P.l*wy[1])/32.0);)
            bottomRightPoint.z = Math.round(sector.getBottomZ() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomRightPoint.y) / VERTICAL_LOOK_SCALE_FACTOR));
            topRightPoint.z = Math.round(sector.getTopZ() - playerInstance.getPosition().z + ((playerInstance.getLookUpDown() * bottomRightPoint.y) / VERTICAL_LOOK_SCALE_FACTOR));

            // If wall is behind player
            if (bottomLeftPoint.y < 0 && bottomRightPoint.y < 0)
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
            bottomLeftPoint.x = MathUtils.round(bottomLeftPoint.x * FIELD_OF_VIEW / bottomLeftPoint.y + X_OFFSET);
            bottomLeftPoint.y = MathUtils.round(bottomLeftPoint.z * FIELD_OF_VIEW / bottomLeftPoint.y + Y_OFFSET);

            bottomRightPoint.x = MathUtils.round(bottomRightPoint.x * FIELD_OF_VIEW / bottomRightPoint.y + X_OFFSET);
            bottomRightPoint.y = MathUtils.round(bottomRightPoint.z * FIELD_OF_VIEW / bottomRightPoint.y + Y_OFFSET);

            topLeftPoint.x = MathUtils.round(topLeftPoint.x * FIELD_OF_VIEW / topLeftPoint.y + X_OFFSET);
            topLeftPoint.y = MathUtils.round(topLeftPoint.z * FIELD_OF_VIEW / topLeftPoint.y + Y_OFFSET);

            topRightPoint.x = MathUtils.round(topRightPoint.x * FIELD_OF_VIEW / topRightPoint.y + X_OFFSET);
            topRightPoint.y = MathUtils.round(topRightPoint.z * FIELD_OF_VIEW / topRightPoint.y + Y_OFFSET);

            //drawWall(wx[0],wx[1],wy[0],wy[1],wy[2],wy[3])
            //Draw points if on screen
            float bottomPointYDistance = MathUtils.round((bottomRightPoint.y - bottomLeftPoint.y));
            float topPointYDistance = MathUtils.round((topRightPoint.y - topLeftPoint.y));
            float xDistance = MathUtils.round(bottomRightPoint.x - bottomLeftPoint.x);
            if (xDistance == 0) {
                xDistance = 1;
            }
            float xStartingPosition = MathUtils.round(bottomLeftPoint.x);

            //Initialize texture values for step calculation (needed to draw the image with perspective)
            textureWrapper = new DoomLikeTextureWrapper(wall.getTextureData());
            //Calculate texel step. wall.getTextureUV().x  is U
            textureWrapper.setHorizontalWallStep(wall.getTextureData().getWidth() * wall.getTextureUV().x / (bottomRightPoint.x - bottomLeftPoint.x));

            //Check which avoids texture distortion on X clip
            if (bottomLeftPoint.x < 0) {
                textureWrapper.subtractHorizontalWallStart(textureWrapper.getHorizontalWallStep() * bottomLeftPoint.x);
            }

            //Clip x
            bottomLeftPoint.x = MathUtils.round(MathUtils.clamp(bottomLeftPoint.x, 0, DoomLikeTestGame.GAME_WIDTH));
            bottomRightPoint.x = MathUtils.round(MathUtils.clamp(bottomRightPoint.x, 0, DoomLikeTestGame.GAME_WIDTH));

            //Draw x vertical lines
            for (float xToRender = bottomLeftPoint.x; xToRender < bottomRightPoint.x; xToRender++) {

                // Get the Y start and end point (0.5 is used for rounding)
                float yBottomPoint = MathUtils.round((bottomPointYDistance * (xToRender - xStartingPosition + 0.5f) / xDistance + bottomLeftPoint.y)); //y1
                float yTopPoint = MathUtils.round((topPointYDistance * (xToRender - xStartingPosition + 0.5f) / xDistance + topLeftPoint.y)); //y2

                //Initialize vertical texture values for step calculation (needed to draw the image with perspective)
                //wall.getTextureUV().y  is V
                float vertStep = wall.getTextureData().getHeight() * wall.getTextureUV().y / (yTopPoint - yBottomPoint);
                textureWrapper.setVerticalWallStart(0);
                textureWrapper.setVerticalWallStep(vertStep);

                //Check which avoids texture distortion on Y clip
                if (yBottomPoint < 0) {
                    textureWrapper.subtractVerticalWallStart(textureWrapper.getVerticalWallStep() * yBottomPoint);
                }
                //Clip Y
                yBottomPoint = MathUtils.round(MathUtils.clamp(yBottomPoint, 0, DoomLikeTestGame.GAME_HEIGHT));
                yTopPoint = MathUtils.round(MathUtils.clamp(yTopPoint, 0, DoomLikeTestGame.GAME_HEIGHT));

                textureData = textureWrapper.getTextureData();
                //Store surface information
                if (backfaceCulling) {
                    if (SectorWrapper.SurfaceShownEnum.BOTTOM.equals(sector.getSurfaceToShow())) {
                        sector.getSurfaceYforXMap().put(xToRender, yBottomPoint);
                        continue;
                    }
                    if (SectorWrapper.SurfaceShownEnum.TOP.equals(sector.getSurfaceToShow())) {
                        sector.getSurfaceYforXMap().put(xToRender, yTopPoint);
                        continue;
                    }
                } else if (sector.getSurfaceYforXMap().containsKey(xToRender)) {
                    //Draw surfaces
                    if (SectorWrapper.SurfaceShownEnum.BOTTOM.equals(sector.getSurfaceToShow())) {
                        drawFloor(playerInstance, xToRender - X_OFFSET, sector.getBottomZ(), sector.getSurfaceYforXMap().get(xToRender) - Y_OFFSET, yTopPoint - Y_OFFSET, playerAngleCurrentCos, playerAngleCurrentSin, sector.getSurfaceScale(), sector.getSurfaceTexture());
                    }
                    if (SectorWrapper.SurfaceShownEnum.TOP.equals(sector.getSurfaceToShow())) {
                        drawFloor(playerInstance, xToRender - X_OFFSET, sector.getTopZ(), yBottomPoint - Y_OFFSET, sector.getSurfaceYforXMap().get(xToRender) - Y_OFFSET, playerAngleCurrentCos, playerAngleCurrentSin, sector.getSurfaceScale(), sector.getSurfaceTexture());
                    }
                }

                // draw vertical line to fill the wall pixel by pixel
                for (float yToRender = yBottomPoint; yToRender < yTopPoint; yToRender++) {
                    //Pick up pixl color for Texture data using texels
                    pixelIndex = MathUtils.floor(textureWrapper.getHorizontalWallStart() % textureData.getWidth()) + textureData.getHeight() * (textureData.getHeight() - MathUtils.floor(textureWrapper.getVerticalWallStart()) - 1);

                    drawPixel(xToRender, yToRender, textureData.getData().get(pixelIndex), wall.getTextureShade());

                    //Increse texel verticalstart by vertical step
                    textureWrapper.addVerticalWallStart(textureWrapper.getVerticalWallStep());
                }
                //Increse texel horizontalStart by horizontal step
                textureWrapper.addHorizontalWallStart(textureWrapper.getHorizontalWallStep());


            }

        }

    }

    /**
     *
     * @param playerInstance
     * @param xToRender
     * @param wallZOffset
     * @param yStart
     * @param yEnd
     * @param playerAngleCurrentCos
     * @param playerAngleCurrentSin
     * @param surfaceScale
     * @param textureData
     */
    private void drawFloor(final PlayerInstance playerInstance, final float xToRender, final float wallZOffset, float yStart, float yEnd, final float playerAngleCurrentCos, final float playerAngleCurrentSin, final float surfaceScale, final DoomLikeTextureData textureData) {

        //index for pixel Color
        int pixelIndex;
        float z;
        float floorX, floorY, rotationX, rotationY;
        //Clamp to remove unwanted ceiling
        float lookUpDownClamped = Math.min(DoomLikeTestGame.GAME_HEIGHT, -playerInstance.getLookUpDown() * LOOK_UP_DOWN_FLOOR_FACTOR);
        float moveUpDownClamped = (playerInstance.getPosition().z - wallZOffset) / Y_OFFSET;
        moveUpDownClamped = moveUpDownClamped == 0 ? 0.001f : moveUpDownClamped;

        //Imported surface tile
        float tile = surfaceScale * TILE_FACTOR;

        //Using lookUpDownClamped to remove ceiling
        for (float y = yStart; y < yEnd; y++) {

            //Calculate with up and camera down up view and position
            z = y + lookUpDownClamped;
            z = (z == 0) ? 0.0001f : z;
            floorX = xToRender / z * moveUpDownClamped * tile;
            floorY = FIELD_OF_VIEW / z * moveUpDownClamped * tile;
            //Calculate camera rotation and position
            rotationX = floorX * playerAngleCurrentSin - floorY * playerAngleCurrentCos + (playerInstance.getPosition().y / PLAYER_OFFSET * tile);
            rotationY = floorX * playerAngleCurrentCos + floorY * playerAngleCurrentSin - (playerInstance.getPosition().x / PLAYER_OFFSET * tile);

            //Remove negative values
            rotationX = rotationX < 0 ? (-rotationX + 1) : rotationX;
            rotationY = rotationY < 0 ? (-rotationY + 1) : rotationY;

            //Round values
            rotationX = Math.round(rotationX);
            rotationY = Math.round(rotationY);

            //Pick up pixl color for Texture data using texels
            //3D Sage left me without a formula. Why you do dis
            pixelIndex = MathUtils.floor(Math.round(rotationX % textureData.getWidth())) + textureData.getHeight() * (textureData.getHeight() - Math.round(rotationY % textureData.getHeight()) - 1);

            drawPixel(xToRender + X_OFFSET, y + Y_OFFSET, textureData.getData().get(pixelIndex));

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
        pointA.z = pointB.z + intersectionFactor * (pointB.z - pointA.z);

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
        shapeDrawer.line(MathUtils.round(x1), MathUtils.round(y1), MathUtils.round(x2), MathUtils.round(y2), 1f);

    }

    /**
     * Draw pixel
     *
     * @param x
     * @param y
     * @param pixelColor
     */
    private void drawPixel(float x, float y, final Color pixelColor) {
        drawPixel(x, y, pixelColor, 0f);

    }

    /**
     * Draw shaded pixel
     *
     * @param x
     * @param y
     * @param pixelColor
     * @param shade
     */
    private void drawPixel(float x, float y, final Color pixelColor, final Float shade) {

        //Shade the pixel if needed
        final Color shadedColor = new Color(pixelColor);
        final float reducedShade = shade / (RBG_CONVERSION_FACTOR * SHADE_REDUCE_FACTOR);
        //Clamp of the shaded pixel is done inside shadedColor.sub
        shadedColor.sub(reducedShade, reducedShade, reducedShade, 0);
        shapeDrawer.setColor(shadedColor);
        shapeDrawer.rectangle(MathUtils.round(x), MathUtils.round(y), 1, 1);

    }

    public void dispose() {
        batch.dispose();
        shapeRendererTexture.dispose();
    }

    @SuppressWarnings("unused")
    @Deprecated
    public void drawSimpleTexture() {
        DoomLikeTextureData textrue = null;// map.getSectors().get(0).getWalls().get(0).getTextureData();
        for (int y = 0; y < textrue.getHeight(); y++) {
            for (int x = 0; x < textrue.getWidth(); x++) {
                drawPixel(x, y, textrue.getData().get(x + textrue.getHeight() * (textrue.getHeight() - y - 1)));
            }
        }
    }

}
