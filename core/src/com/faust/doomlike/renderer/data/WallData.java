package com.faust.doomlike.renderer.data;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Wall data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class WallData {

    final List<Vector3> points = new ArrayList<Vector3>();

    public List<Vector3> getPoints() {
        return points;
    }

    public void addPoints(Vector3 pointsToAdd) {
        if (points.size() == 4) {
            throw new GdxRuntimeException("points should not be more than 4!");

        }
        points.add(pointsToAdd);
    }
}
