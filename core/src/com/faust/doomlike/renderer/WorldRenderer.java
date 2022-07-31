package com.faust.doomlike.renderer;

import com.faust.doomlike.test.PlayerInstance;
import com.faust.doomlike.utils.MapWrapper;

/**
 * World renderer interface
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public interface WorldRenderer {

    void drawWorld(MapWrapper map, PlayerInstance playerInstance);

    void dispose();
}
