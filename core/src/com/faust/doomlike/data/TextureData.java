package com.faust.doomlike.data;

import java.util.List;

/**
 * Texture data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class TextureData {

    private int width;
    private int height;
    private List<Float> data;

    public TextureData(int width, int height, List<Float> data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Float> getData() {
        return data;
    }
}
