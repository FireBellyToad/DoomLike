package com.faust.doomlike.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class MapData {

    final private List<WallData> walls = new ArrayList<WallData>();
    final private List<SectorData> sectors = new ArrayList<SectorData>();

    final private Map<String, String> sectorWallsMap = new HashMap<>();

    public List<SectorData> getSectors() {
        return sectors;
    }
}
