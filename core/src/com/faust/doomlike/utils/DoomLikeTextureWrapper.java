package com.faust.doomlike.utils;

import com.faust.doomlike.data.TextureData;

/**
 * Texture wrapper class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeTextureWrapper {

    final TextureData textureData;
    float horizontalWallStart;
    float horizontalWallStep;
    float verticalWallStart;
    float verticalWallStep;

    public DoomLikeTextureWrapper(TextureData textureData) {
        this.textureData = textureData;
    }

    public TextureData getTextureData() {
        return textureData;
    }

    public float getHorizontalWallStart() {
        return horizontalWallStart;
    }

    public void setHorizontalWallStart(float horizontalWallStart) {
        this.horizontalWallStart = horizontalWallStart;
    }

    public float getHorizontalWallStep() {
        return horizontalWallStep;
    }

    public void setHorizontalWallStep(float horizontalWallStep) {
        this.horizontalWallStep = horizontalWallStep;
    }

    public float getVerticalWallStart() {
        return verticalWallStart;
    }

    public void setVerticalWallStart(float verticalWallStart) {
        this.verticalWallStart = verticalWallStart;
    }

    public float getVerticalWallStep() {
        return verticalWallStep;
    }

    public void setVerticalWallStep(float verticalWallStep) {
        this.verticalWallStep = verticalWallStep;
    }

    public void addHorizontalWallStart(float toAdd) {
        this.horizontalWallStart +=toAdd;
    }

    public void subtractHorizontalWallStart(float toSub) {
        this.horizontalWallStart -=toSub;
    }

    public void addVerticalWallStart(float toAdd) {
        this.verticalWallStart +=toAdd;
    }

    public void subtractVerticalWallStart(float toSub) {
        this.verticalWallStart -=toSub;
    }
}
