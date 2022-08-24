package com.faust.doomlike.renderer;

/**
 * World renderer interface
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public interface WorldRenderer <T>{

    void drawWorld(float stateTime, T world);

    void dispose();
}
