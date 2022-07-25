package com.faust.doomlike.utils;

import com.badlogic.gdx.math.Vector2;
import com.faust.doomlike.data.MapData;
import com.faust.doomlike.data.SectorData;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for rendering Sector
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class MapWrapper {

    private final MapData mapData;
    private final List<SectorWrapper> sectors = new ArrayList<>();

    public MapWrapper(MapData mapData) {
        this.mapData = mapData;

        this.mapData.getSectors().forEach(sd -> sectors.add(new SectorWrapper(sd)));
    }

    public List<SectorWrapper> getSectors() {
        return sectors;
    }
}
