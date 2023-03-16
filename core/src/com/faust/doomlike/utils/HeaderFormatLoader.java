package com.faust.doomlike.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.TextureData;
import com.faust.doomlike.data.WallData;

import java.util.*;

/**
 * Temporary class! Should be deprecated ASAP
 * Special loader for texture in , format (see https://www.youtube.com/watch?v=fRu8kjXvkdY)
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class HeaderFormatLoader implements Loader {

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

    //TODO REMOVE FROM HERE
    private Map<String,TextureData> map;

    private static final int SECTORS_NUMBER_INDEX = 0;
    //These indexes are calculated in a hyphotetical 0 sector map for rights offsetting
    private static final int SECTOR_DATA_INDEX = 1;
    private static final int WALLS_DATA_START_INDEX = 2;
    private static final int PLAYER_POSITION_INDEX = 3;

    private static final int WALLS_START_INDEX = 0;
    private static final int WALLS_END_INDEX = 1;
    private static final int BOTTOM_Z_INDEX = 2;
    private static final int TOP_Z_INDEX = 3;
    private static final int TEXTURE_SCALE_INDEX = 4;

    private static final int DATA_ARRAY_INDEX = 5;

    public Map<String, TextureData> loadTextures() {
        this.map = new HashMap<>();
        String name;
        FileHandle file;
        int endOfFile;
        List<Float> data;
        for (int textureNumber = 0; textureNumber < 20; textureNumber++) {
            //Get file from name
            name = ((textureNumber < 10) ? "T_0" : "T_") + textureNumber+".h";
            file = Gdx.files.internal("textures/" + name);
            String[] textureString = file.readString().split("\n");

            data = new ArrayList<>();
            //Calculate end of file
            endOfFile = textureString.length - DATA_ARRAY_INDEX - 1;
            //Extract data from file
            for (int row = 0; row < endOfFile; row++) {
                for (String substr : textureString[DATA_ARRAY_INDEX + row].split(", ")) {
                    data.add(Float.parseFloat(substr.trim()));
                }
            }
            this.map.put(name, new TextureData(16,16, data));
        }
        return map;
    }

    public MapData loadMap() {
        //TODO improve using json files and better file handling!
        FileHandle file = Gdx.files.absolute("E:\\Repositories\\DoomLike\\core\\assets\\levels\\level.h");
        String levelString = file.readString();
        MapData testMapData = new MapData();

        //Organize data
        String[] levelStringList = levelString.split("\r\n");

        //

        //TODO file validation (?)
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
            sectorModel.setBottomColor(colorList.get(0));
            sectorModel.setTopColor(colorList.get(1));
            sectorModel.setSurfaceTexture(Float.parseFloat(sectorData[TEXTURE_SCALE_INDEX]));

            //Select which walls will be added to this sector
            sectorWallsStart = Integer.parseInt(sectorData[WALLS_START_INDEX]);
            sectorWallsEnd = Integer.parseInt(sectorData[WALLS_END_INDEX]);

            //Get data for each wall
            for (int w = sectorWallsStart; w < sectorWallsEnd; w++) {
                //extract wall data
                wallData = levelStringList[WALLS_DATA_START_INDEX + sectorsNumber + w].split(" ");

                textureNumber = Integer.parseInt(wallData[4]);

                sectorModel.getWalls().add(new WallData(
                        Float.parseFloat(wallData[0]),
                        Float.parseFloat(wallData[1]),
                        Float.parseFloat(wallData[2]),
                        Float.parseFloat(wallData[3]),
                        Float.parseFloat(wallData[5]),
                        Float.parseFloat(wallData[6]),
                        map.get(((textureNumber+1 < 10) ? "T_0" : "T_") + textureNumber +".h")));
            }

            testMapData.getSectors().add(sectorModel);
        }
        return testMapData;
    }
}
