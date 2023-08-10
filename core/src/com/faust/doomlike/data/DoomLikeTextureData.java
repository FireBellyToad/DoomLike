package com.faust.doomlike.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Objects;

/**
 * Texture data class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class DoomLikeTextureData {

    private final int width;
    private final int height;
    private final List<Color> data;
    private Texture gdxTexture;

    public DoomLikeTextureData(int width, int height, List<Color> data) {
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

    /***
     * Convert and keep the texture in a Gdx Friendly format
     *
     * @return a Gdx Texture
     */
    public Texture toGdxTexture(){

        if(Objects.isNull(gdxTexture)){

            Color color;
            Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    color = data.get(x + height * (height - y - 1));
                    pixmap.setColor(color);
                    pixmap.drawPixel(x, y);
                }
            }
            gdxTexture = new Texture(pixmap);
            gdxTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            gdxTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            pixmap.dispose();
        }
        return gdxTexture;

    }
}
