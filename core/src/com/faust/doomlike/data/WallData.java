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
    final Vector2 textureUV;
    final TextureData textureData;

    public WallData(float x1, float y1, float x2, float y2, float u, float v, TextureData textureData) {
        bottomLeftPoint = new Vector2(x1, y1);
        bottomRightPoint = new Vector2(x2, y2);
        this.textureData = textureData;
        this.wallUuid = UUID.randomUUID().toString();
        this.textureUV = new Vector2(u, v);
    }

    public Vector2 getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public Vector2 getBottomRightPoint() {
        return bottomRightPoint;
    }

    public Vector2 getTextureUV() {
        return textureUV;
    }

    public String getWallUuid() {
        return wallUuid;
    }

    public TextureData getTextureData() {
        return textureData;
    }
}
