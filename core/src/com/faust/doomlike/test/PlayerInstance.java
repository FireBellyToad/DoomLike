package com.faust.doomlike.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.faust.doomlike.renderer.DoomLikeRenderer;

/**
 * Test Player class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class PlayerInstance implements InputProcessor {

    private final Vector3 position = new Vector3(70, -110, 20);
    private int angle = 0;
    private int lookUpDown = 0;

    public Vector3 getPosition() {
        return position;
    }

    public int getAngle() {
        return angle;
    }

    public int getLookUpDown() {
        return lookUpDown;
    }

    @Override
    public boolean keyDown(int keycode) {

        int deltaX = (int) (MathUtils.sinDeg(angle) * 10);
        int deltaY = (int) (MathUtils.cosDeg(angle) * 10);

        switch (keycode) {
            case Input.Keys.W:
                //Move forward
                position.x += deltaX;
                position.y += deltaY;
                break;
            case Input.Keys.S:
                //Move backward
                position.x -= deltaX;
                position.y -= deltaY;
                break;
            case Input.Keys.A:
                //Strafe left
                position.x += deltaY;
                position.y -= deltaX;
                break;
            case Input.Keys.D:
                //Strafe right
                position.x -= deltaY;
                position.y += deltaX;
                break;
            case Input.Keys.Q:
                //rotate left
                angle -= 4;
                if (angle < 0) {
                    angle += 360;
                }
                break;
            case Input.Keys.E:
                //rotate right
                angle += 4;
                if (angle > 359) {
                    angle -= 360;
                }
                break;
            case Input.Keys.R:
                //rotate up
                lookUpDown += 1;
                break;
            case Input.Keys.F:
                //rotate down
                lookUpDown -= 1;
                break;
            case Input.Keys.T:
                //fly up
                position.z += 4;
                break;
            case Input.Keys.G:
                //fly down
                position.z -= 4;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                break;
            case Input.Keys.S:
                break;
            case Input.Keys.A:
                break;
            case Input.Keys.D:
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public String toString() {
        return getPosition().toString() + " - angle: " + angle + " - lookUpDown: " + lookUpDown;
    }
}
