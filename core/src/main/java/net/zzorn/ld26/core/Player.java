package net.zzorn.ld26.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 */
public class Player extends Entity {

    private InputProcessor inputProcessor;
    private int upKey = Input.Keys.W;
    private int downKey = Input.Keys.S;
    private int rightKey = Input.Keys.D;
    private int leftKey = Input.Keys.A;


    public Player(float x, float y, float z, float size, String textureName) {
        super(x, y, z, size, textureName);

        this.inputProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }
        };
    }

    @Override
    public void update(float deltaTime) {
        double fd = 0;
        if (Gdx.input.isKeyPressed(upKey)) fd += 1;
        if (Gdx.input.isKeyPressed(downKey)) fd -= 1;
        //setForwardDelta(fd);

        double dd = 0;
        if (Gdx.input.isKeyPressed(leftKey)) dd -= 1;
        if (Gdx.input.isKeyPressed(rightKey)) dd += 1;
        //setDirectionDelta(dd);

        super.update(deltaTime);
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
