package com.faust.doomlike.test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Test Player class
 *
 * @author Jacopo "Faust" Buttiglieri
 */
public class PlayerInstance implements InputProcessor {

    private final Vector3 position = new Vector3(70, -110, 20);
    private int angle = 0;
    private int lookUpDown = 0;

    private final Vector3 deltaPosition = Vector3.Zero.cpy();
    private int deltaAngle = 0;
    private int deltaLookUpDown = 0;
    private boolean reloadLevel = false;

    public void doLogic(){

        position.add(deltaPosition);
        angle += deltaAngle;

        if (angle < 0) {
            angle += 360;
        } else if (angle > 359) {
            angle -= 360;
        }

        lookUpDown += deltaLookUpDown;
    }

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

        int deltaX = (int) (MathUtils.sinDeg(angle) * 5);
        int deltaY = (int) (MathUtils.cosDeg(angle) * 5);

        switch (keycode) {
            case Input.Keys.W:
                //Move forward
                deltaPosition.x += deltaX;
                deltaPosition.y += deltaY;
                break;
            case Input.Keys.S:
                //Move backward
                deltaPosition.x -= deltaX;
                deltaPosition.y -= deltaY;
                break;
            case Input.Keys.A:
                //Strafe left
                deltaPosition.x -= deltaY;
                deltaPosition.y += deltaX;
                break;
            case Input.Keys.D:
                //Strafe right
                deltaPosition.x += deltaY;
                deltaPosition.y -= deltaX;
                break;
            case Input.Keys.Q:
                //rotate left
                deltaAngle -= 2;
                break;
            case Input.Keys.E:
                //rotate right
                deltaAngle += 2;
                break;
            case Input.Keys.R:
                //rotate up
                deltaLookUpDown -= 1;
                break;
            case Input.Keys.F:
                //rotate down
                deltaLookUpDown += 1;
                break;
            case Input.Keys.T:
                //fly up
                deltaPosition.z += 2;
                break;
            case Input.Keys.G:
                //fly down
                deltaPosition.z -= 2;
                break;
            case Input.Keys.ENTER:
                reloadLevel = true;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.S:
            case Input.Keys.A:
            case Input.Keys.D:
                //Move forward
                deltaPosition.x = 0;
                deltaPosition.y = 0;
                break;
            case Input.Keys.Q:
            case Input.Keys.E:
                //rotate right
                deltaAngle = 0;
                break;
            case Input.Keys.R:
            case Input.Keys.F:
                //rotate down
                deltaLookUpDown = 0;
                break;
            case Input.Keys.T:
            case Input.Keys.G:
                deltaPosition.z = 0;
                break;
        }


        return false;
    }

    public boolean isReloadLevel() {
        return reloadLevel;
    }

    public void setReloadLevel(boolean reloadLevel) {
        this.reloadLevel = reloadLevel;
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
