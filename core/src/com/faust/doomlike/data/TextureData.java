package com.faust.doomlike.data;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

/**
 * Texture data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class TextureData {

    private final int width;
    private final int height;
    private final List<Color> data;

    public TextureData(int width, int height, List<Color> data) {
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

    public List<Color> getData() {
        return data;
    }
}
