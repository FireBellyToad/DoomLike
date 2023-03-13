package com.faust.doomlike.data;

/**
 * Texture data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class TextureData {

    private int width;
    private int height;
    private String name;

    public TextureData(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }
}
