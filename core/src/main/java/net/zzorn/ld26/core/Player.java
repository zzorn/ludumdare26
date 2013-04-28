package net.zzorn.ld26.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 *
 */
public class Player extends Entity {

    private static final Vector3 UP = new Vector3(0, 1, 0);
    private static final Vector3 RIGHT = new Vector3(1, 0, 0);
    private static final Quaternion ROTATE_RIGHT = new Quaternion(UP, -90);
    private static final Quaternion ROTATE_UP = new Quaternion(RIGHT, 90);
    private final Quaternion tmpRot = new Quaternion();

    private final PerspectiveCamera camera;
    private InputProcessor inputProcessor;
    private int upKey = Input.Keys.W;
    private int downKey = Input.Keys.S;
    private int rightKey = Input.Keys.D;
    private int leftKey = Input.Keys.A;
    private int turnRKey = Input.Keys.RIGHT;
    private int turnLKey = Input.Keys.LEFT;
    private int turnUKey = Input.Keys.UP;
    private int turnDKey = Input.Keys.DOWN;

    private float turnSpeedDegPerSec = 30;

    private int shootKey = Input.Keys.SPACE;
    private Vector3 temp = new Vector3();

    public Player(float x, float y, float z, float size, String textureName, PerspectiveCamera camera) {
        super(x, y, z, size, textureName);
        this.camera = camera;

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
        camera.position.set(getPos());
        camera.direction.set(getDirection()).nor();
        camera.update();

        final Vector3 direction = getDirection();

        // Forward
        temp.set(direction);
        float fd = 0;
        if (Gdx.input.isKeyPressed(upKey)) fd += 1;
        if (Gdx.input.isKeyPressed(downKey)) fd -= 1;
        temp.scl(fd);
        getAcc().add(temp);

        // Strafe
        temp.set(direction);
        ROTATE_RIGHT.transform(temp);
        float dd = 0;
        if (Gdx.input.isKeyPressed(leftKey)) dd -= 1;
        if (Gdx.input.isKeyPressed(rightKey)) dd += 1;
        temp.scl(dd);
        getAcc().add(temp);

        // Turn
        int turnUp = 0;
        int turnRight = 0;
        if (Gdx.input.isKeyPressed(turnUKey)) turnUp -= 1;
        if (Gdx.input.isKeyPressed(turnDKey)) turnUp += 1;
        if (Gdx.input.isKeyPressed(turnRKey)) turnRight -= 1;
        if (Gdx.input.isKeyPressed(turnLKey)) turnRight += 1;
        temp.set(direction);
        ROTATE_RIGHT.transform(temp);
        if (turnUp != 0) {
            tmpRot.set(temp, deltaTime * turnUp * turnSpeedDegPerSec);
            tmpRot.transform(direction);
        }
        if (turnRight != 0) {
            tmpRot.set(UP, deltaTime * turnRight * turnSpeedDegPerSec);
            tmpRot.transform(direction);
        }
        direction.nor();


        if (Gdx.input.isKeyPressed(shootKey)) {
            shoot();
        }

        super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch spriteBatch, TextureAtlas atlas) {
        // No need to render, 1st person perspective
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
