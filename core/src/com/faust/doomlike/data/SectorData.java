package com.faust.doomlike.data;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sector data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class SectorData {

    final private String sectorUuid;
    final private Vector2 origin = new Vector2();
    private float floorHeight;
    private float ceilingHeight;
    private float depth;

    final private List<WallData> walls = new ArrayList<WallData>();

    public SectorData() {
        this.sectorUuid = UUID.randomUUID().toString();
    }


    public Vector2 getOrigin() {
        return origin;
    }

    public float getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(float floorHeight) {
        this.floorHeight = floorHeight;
    }

    public float getCeilingHeight() {
        return ceilingHeight;
    }

    public void setCeilingHeight(float ceilingHeight) {
        this.ceilingHeight = ceilingHeight;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public List<WallData> getWalls() {
        return walls;
    }

    public String getSectorUuid() {
        return sectorUuid;
    }
}
