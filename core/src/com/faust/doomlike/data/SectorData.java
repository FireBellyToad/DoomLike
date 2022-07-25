package com.faust.doomlike.data;

import com.badlogic.gdx.graphics.Color;
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
    private float bottomHeight;
    private float topHeight;
    private Color bottomColor;
    private Color topColor;

    final private List<WallData> walls = new ArrayList<WallData>();

    public SectorData() {
        this.sectorUuid = UUID.randomUUID().toString();
    }


    public Vector2 getOrigin() {
        return origin;
    }

    public float getBottomHeight() {
        return bottomHeight;
    }

    public void setBottomHeight(float bottomHeight) {
        this.bottomHeight = bottomHeight;
    }

    public float getTopHeight() {
        return topHeight;
    }

    public void setTopHeight(float topHeight) {
        this.topHeight = topHeight;
    }

    public List<WallData> getWalls() {
        return walls;
    }

    public Color getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(Color bottomColor) {
        this.bottomColor = bottomColor;
    }

    public Color getTopColor() {
        return topColor;
    }

    public void setTopColor(Color topColor) {
        this.topColor = topColor;
    }

    public String getSectorUuid() {
        return sectorUuid;
    }
}
