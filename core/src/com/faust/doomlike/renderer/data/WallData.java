package com.faust.doomlike.renderer.data;

import com.badlogic.gdx.math.Vector3;

/**
 * Wall data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class WallData {

    final Vector3 bottomLeftPoint = new Vector3();
    final Vector3 bottomRightPoint = new Vector3();
    final Vector3 topLeftPoint = new Vector3();
    final Vector3 topRightPoint = new Vector3();

    public Vector3 getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public Vector3 getBottomRightPoint() {
        return bottomRightPoint;
    }

    public Vector3 getTopLeftPoint() {
        return topLeftPoint;
    }

    public Vector3 getTopRightPoint() {
        return topRightPoint;
    }
}
