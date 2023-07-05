package com.faust.doomlike.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Map data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class MapData {

    final private List<SectorData> sectors = new ArrayList<>();

    public List<SectorData> getSectors() {
        return sectors;
    }
}
