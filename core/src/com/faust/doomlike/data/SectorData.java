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
    private float bottomZ;
    private float topZ;
    private float surfaceScale;
    private DoomLikeTextureData surfaceTexture;

    final private List<WallData> walls = new ArrayList<>();

    public SectorData() {
        this.sectorUuid = UUID.randomUUID().toString();
    }


    public Vector2 getOrigin() {
        return origin;
    }

    public float getBottomZ() {
        return bottomZ;
    }

    public void setBottomZ(float bottomZ) {
        this.bottomZ = bottomZ;
    }

    public float getTopZ() {
        return topZ;
    }

    public void setTopZ(float topZ) {
        this.topZ = topZ;
    }

    public List<WallData> getWalls() {
        return walls;
    }

    public float getSurfaceScale() {
        return surfaceScale;
    }

    public void setSurfaceScale(float surfaceScale) {
        this.surfaceScale = surfaceScale;
    }

    public DoomLikeTextureData getSurfaceTexture() {
        return surfaceTexture;
    }

    public void setSurfaceTexture(DoomLikeTextureData surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
    }

    public String getSectorUuid() {
        return sectorUuid;
    }
}
