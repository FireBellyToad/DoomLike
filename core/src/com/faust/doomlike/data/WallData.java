package com.faust.doomlike.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

/**
 * Wall data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class WallData {

    final private String wallUuid;
    final Vector2 bottomLeftPoint;
    final Vector2 bottomRightPoint;
    final Color color;

    public WallData(float x1, float y1, float x2, float y2, Color wallColor) {
        bottomLeftPoint = new Vector2(x1,y1);
        bottomRightPoint = new Vector2(x2,y2);
        this.color = wallColor;
        this.wallUuid = UUID.randomUUID().toString();
    }

    public Vector2 getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public Vector2 getBottomRightPoint() {
        return bottomRightPoint;
    }

    public String getWallUuid() {
        return wallUuid;
    }

    public Color getColor() {
        return color;
    }
}
