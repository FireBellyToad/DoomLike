package com.faust.doomlike.utils;

import com.badlogic.gdx.graphics.Color;
import com.faust.doomlike.data.SectorData;
import com.faust.doomlike.data.WallData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class for rendering Sector
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class SectorWrapper {

    public enum SurfaceShownEnum {
        TOP, BOTTOM, NONE
    }

    private final SectorData sectorData;
    private final Map<Float,Float> surfaceYforXMap =new HashMap<>();
    private SurfaceShownEnum surfaceToShow = SurfaceShownEnum.NONE;
    private float depth = 0;

    public SectorWrapper(SectorData sectorData) {
        this.sectorData = sectorData;
    }

    public Map<Float,Float>  getSurfaceYforXMap() {
        return surfaceYforXMap;
    }

    public SurfaceShownEnum getSurfaceToShow() {
        return surfaceToShow;
    }

    public void setSurfaceToShow(SurfaceShownEnum surfaceToShow) {
        this.surfaceToShow = surfaceToShow;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public void addToDepth(float depth) {
        this.depth += depth;
    }

    public List<WallData> getWalls(){
        return sectorData.getWalls();
    }

    public float getBottomHeight() {
        return sectorData.getBottomHeight();
    }

    public float getTopHeight() {
        return sectorData.getTopHeight();
    }

    public Color getTopColor() {
        return sectorData.getTopColor();
    }

    public Color getBottomColor() {
        return sectorData.getBottomColor();
    }

    public String getSectorUuid() {
        return this.sectorData.getSectorUuid();
    }
}
