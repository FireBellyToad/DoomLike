package com.faust.doomlike.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.DoomLikeTextureData;
import com.faust.doomlike.data.WallData;
import com.faust.doomlike.renderer.impl.DoomLikeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Temporary class! Should be deprecated ASAP
 * Special loader for texture in , format (see https://www.youtube.com/watch?v=fRu8kjXvkdY)
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class HeaderFormatLoader {

    private final static List<Color> colorList = new ArrayList<Color>() {{
        this.add(new Color(0xffff00ff));
        this.add(new Color(0xaaaa00ff));
        this.add(new Color(0xff0000ff));
        this.add(new Color(0xaa0000ff));
        this.add(new Color(0x00ff00ff));
        this.add(new Color(0x00aa00ff));
        this.add(new Color(0x0000ffff));
        this.add(new Color(0x0000aaff));
    }};

    private static final int SECTORS_NUMBER_INDEX = 0;
    //These indexes are calculated in a hyphotetical 0 sector map for rights offsetting
    private static final int SECTOR_DATA_INDEX = 1;
    private static final int WALLS_DATA_START_INDEX = 2;
    private static final int PLAYER_POSITION_INDEX = 3;

    private static final int WALLS_START_INDEX = 0;
    private static final int WALLS_END_INDEX = 1;
    private static final int BOTTOM_Z_INDEX = 2;
    private static final int TOP_Z_INDEX = 3;
    private static final int TEXTURE_NUMBER_INDEX = 4;
    private static final int TEXTURE_SCALE_INDEX = 5;

    private static final int TEXTURE_HEIGHT_INDEX = 0;
    private static final int TEXTURE_WIDTH_INDEX = 1;
    private static final int DATA_ARRAY_INDEX = 5;

    public Map<String, DoomLikeTextureData> loadTextures() {

        Map<String, DoomLikeTextureData> texturesMap = new HashMap<>();

        //Placeholder empty variables
        String name;
        FileHandle file;
        int endOfFile;
        int textureWidth;
        int textureHeight;
        List<Color> data;
        Color pixelColor = new Color();
        int rgbCounter = 0;
        String[] textureString;
        float extractedColorFromString;

        //Parse file
        for (int textureNumber = 0; textureNumber < 20; textureNumber++) {

            //Get file from name (adjusted if less than 10)
            name = ((textureNumber < 10) ? "T_0" : "T_") + textureNumber + ".h";
            file = Gdx.files.internal("textures/" + name);
            textureString = file.readString().split("\r\n");

            //set textureWidth and textureHeight
            textureWidth = Integer.parseInt(textureString[TEXTURE_WIDTH_INDEX].split(" ")[2]);
            textureHeight = Integer.parseInt(textureString[TEXTURE_HEIGHT_INDEX].split(" ")[2]);

            data = new ArrayList<>();
            //Calculate end of file
            endOfFile = textureString.length - DATA_ARRAY_INDEX - 1;
            //Extract data from file
            for (int fileRow = 0; fileRow < endOfFile; fileRow++) {
                //Cycle each fileRow to create a list of RGB colors
                for (String substr : textureString[DATA_ARRAY_INDEX + fileRow].split(", ")) {

                    //Map data to libGdx color
                    extractedColorFromString = Float.parseFloat(substr.trim()) / DoomLikeRenderer.RBG_CONVERSION_FACTOR;

                    switch (rgbCounter) {
                        case 0: {
                            //RED
                            pixelColor.r = extractedColorFromString;
                            rgbCounter++;
                            break;
                        }
                        case 1: {
                            //GREEN
                            pixelColor.g = extractedColorFromString;
                            rgbCounter++;
                            break;
                        }
                        case 2: {
                            //BLUE
                            pixelColor.b = extractedColorFromString;
                            pixelColor.a = 1; // Alpha must be 1
                            rgbCounter = 0;
                            data.add(pixelColor);

                            //Prepare new color for next iteration
                            pixelColor = new Color();
                            //restart
                            break;
                        }
                    }

                }
            }
            texturesMap.put(name, new DoomLikeTextureData(textureWidth, textureHeight, data));
        }
        return texturesMap;
    }

    /**
     * Map Example
     *
     * 2
     * 0 4 0 40 1 4
     * 4 8 0 40 1 4
     * 8
     * 256 96 448 96 0 1 1 0
     * 448 96 448 224 0 1 1 90
     * 448 224 256 224 0 1 1 0
     * 256 224 256 96 0 1 1 75
     * 96 384 128 384 0 1 1 0
     * 128 384 128 352 0 1 1 90
     * 128 352 96 352 0 1 1 0
     * 96 352 96 384 0 1 1 90
     *
     * 288 48 30 0 0
     *
     * @return
     */
    public MapData loadMap(Map<String, DoomLikeTextureData> texturesMap) {
        //TODO improve using json files and better file handling!
        FileHandle file = Gdx.files.absolute("E:\\Repositories\\DoomLike\\core\\assets\\levels\\level.h");
        String levelString = file.readString();
        MapData testMapData = new MapData();
        String sectorTextureName;

        //Organize data
        String[] levelStringList = levelString.split("\r\n");

        //First get the number of sectors
        int sectorsNumber = Integer.parseInt(levelStringList[SECTORS_NUMBER_INDEX]);
        //Temp variables
        int sectorWallsStart;
        int sectorWallsEnd;
        int textureNumber;
        String[] sectorData;
        String[] wallData;

        for (int s = 0; s < sectorsNumber; s++) {
            //Get all the sector data. NOTE: we are not using wallStart and wallEnd (index 0 and 1 of the
            //sectorData array).
            sectorData = levelStringList[SECTOR_DATA_INDEX + s].split(" ");
            SectorData sectorModel = new SectorData();
            sectorModel.setBottomZ(Float.parseFloat(sectorData[BOTTOM_Z_INDEX]));
            sectorModel.setTopZ(Float.parseFloat(sectorData[TOP_Z_INDEX]));

            //Extract texture
            textureNumber = Integer.parseInt(sectorData[TEXTURE_NUMBER_INDEX]);
            sectorTextureName = ((textureNumber < 10) ? "T_0" : "T_") + textureNumber + ".h";
            sectorModel.setSurfaceTexture(texturesMap.get(sectorTextureName));

            //Extract texture scale
            sectorModel.setSurfaceScale(Integer.parseInt(sectorData[TEXTURE_SCALE_INDEX]));

            //Select which walls will be added to this sector
            sectorWallsStart = Integer.parseInt(sectorData[WALLS_START_INDEX]);
            sectorWallsEnd = Integer.parseInt(sectorData[WALLS_END_INDEX]);

            //Get data for each wall
            for (int w = sectorWallsStart; w < sectorWallsEnd; w++) {
                //extract wall data
                wallData = levelStringList[WALLS_DATA_START_INDEX + sectorsNumber + w].split(" ");

                textureNumber = Integer.parseInt(wallData[4]);

                //Create wall to save
                sectorModel.getWalls().add(new WallData(
                        Float.parseFloat(wallData[0]), // x1
                        Float.parseFloat(wallData[1]), // y1
                        Float.parseFloat(wallData[2]), // x2
                        Float.parseFloat(wallData[3]), // y2
                        Float.parseFloat(wallData[5]), // texture U
                        Float.parseFloat(wallData[6]), // texture V
                        Integer.parseInt(wallData[7]), // shade
                        texturesMap.get(((textureNumber + 1 < 10) ? "T_0" : "T_") + textureNumber + ".h")));
            }

            testMapData.getSectors().add(sectorModel);
        }
        return testMapData;
    }
}
