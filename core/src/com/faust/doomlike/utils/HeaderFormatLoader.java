package com.faust.doomlike.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;

import java.util.*;

/**
 * Temporary class! Should be deprecated ASAP
 * Special loader for texture in , format (see https://www.youtube.com/watch?v=fRu8kjXvkdY)
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class HeaderFormatLoader {

    private final static Color yellow = new Color(0xffff00ff);
    private final static Color darkYellow = new Color(0xaaaa00ff);
    private final static Color red = new Color(0xff0000ff);
    private final static Color darkRed = new Color(0xaa0000ff);
    private final static Color green = new Color(0x00ff00ff);
    private final static Color darkGreen = new Color(0x00aa00ff);
    private final static Color blue = new Color(0x0000ffff);
    private final static Color darkBlue = new Color(0x0000aaff);

    private static final int SECTORS_NUMBER_INDEX = 0;
    //These indexes are calculated in a hyphotetical 0 sector map for rights offsetting
    private static final int SECTOR_DATA_INDEX = 1;
    private static final int WALLS_DATA_START_INDEX = 2;

    private static final int BOTTOM_Z_INDEX = 2;
    private static final int TOP_Z_INDEX = 3;

    public Map<String, List<Character>> loadTexture() {
        Map<String, List<Character>> map = new HashMap<>();
        String name;
        FileHandle file;
        for (int i = 0; i < 20; i++) {
            name = "T_" + i;
            file = Gdx.files.internal("textures/T" + name);
            Gdx.app.log("DEBUG", file.toString());
//            map.put(name, file.toString());
        }
        return map;
    }

    public MapData loadMap() {
        //TODO improve using json files!
        FileHandle file = Gdx.files.internal("levels/level.h");
        String levelString = file.readString();
        MapData testMapData = new MapData();

        //Organize data
        String[] levelStringList = levelString.split("\r\n");

        //

        //TODO file validation (?)
        //First get the number of sectors
        int sectorsNumber = Integer.parseInt(levelStringList[SECTORS_NUMBER_INDEX]);
        int wallsNumber = Integer.parseInt(levelStringList[SECTOR_DATA_INDEX+sectorsNumber]);
        String[] sectorData;
        String[] wallData;

        for (int s = 0; s < sectorsNumber; s++) {
            //Get all the sector data. NOTE: we are not using wallStart and wallEnd (index 0 and 1 of the
            //sectorData array).
            sectorData = levelStringList[SECTOR_DATA_INDEX].split(" ");
            SectorData sectorModel = new SectorData();
            sectorModel.setBottomZ(Float.parseFloat(sectorData[BOTTOM_Z_INDEX]));
            sectorModel.setTopZ(Float.parseFloat(sectorData[TOP_Z_INDEX]));
            sectorModel.setBottomColor(yellow);
            sectorModel.setTopColor(darkYellow);
            //Get data for each wall
            for (int w = 0; w < wallsNumber; w++) {
                wallData = levelStringList[WALLS_DATA_START_INDEX + sectorsNumber + w].split(" ");

                sectorModel.getWalls().add(new WallData(
                        Float.parseFloat(wallData[0]),
                        Float.parseFloat(wallData[1]),
                        Float.parseFloat(wallData[2]),
                        Float.parseFloat(wallData[3]),// FIXME skipping 4, is texterNumber
                        Float.parseFloat(wallData[5]),
                        Float.parseFloat(wallData[6]),
                        darkRed));
            }
            ;
            testMapData.getSectors().add(sectorModel);
        }
        return testMapData;
    }
}