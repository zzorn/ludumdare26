package net.zzorn.ld26.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.*;
import static net.zzorn.ld26.core.MathUtils.*;

/**
 *
 */
public class Entity extends Sprite {

    private static final double Tau = PI * 2;

    private double direction;
    private double directionDelta;
    private double forwardDelta;
    private double rightDelta;
    private double dx;
    private double dy;
    private double x;
    private double y;

    private double inertia = 0.98;

    private double turnSpeed = 1;
    private double forwardSpeed = 200;
    private double strafeSpeed = 200;

    public Entity(double x, double y, double w, double h, TextureRegion textureRegion) {
        super(textureRegion);

        this.x = x;
        this.y = y;
        setPosition((float) x, (float) y);
        setSize((float) w, (float) h);
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(double turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public double getForwardSpeed() {
        return forwardSpeed;
    }

    public void setForwardSpeed(double forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public double getStrafeSpeed() {
        return strafeSpeed;
    }

    public void setStrafeSpeed(double strafeSpeed) {
        this.strafeSpeed = strafeSpeed;
    }

    public void update(float deltaTime) {

        direction += directionDelta * turnSpeed * deltaTime;

        double oldDx = dx;
        double oldDy = dy;

        dx = cos(direction * Tau) * forwardDelta * forwardSpeed * deltaTime;
        dy = sin(direction * Tau) * forwardDelta * forwardSpeed * deltaTime;
        dx += cos((direction - 0.25) * Tau) * rightDelta * strafeSpeed * deltaTime;
        dy += sin((direction - 0.25) * Tau) * rightDelta * strafeSpeed * deltaTime;

        dx = mix(inertia, dx, oldDx);
        dy = mix(inertia, dy, oldDy);

        x += dx;
        y += dy;

        setPosition((float) x, (float) y);
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void render(SpriteBatch spriteBatch, TextureAtlas atlas) {
        draw(spriteBatch);
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirectionDelta() {
        return directionDelta;
    }

    public void setDirectionDelta(double directionDelta) {
        this.directionDelta = directionDelta;
    }

    public double getForwardDelta() {
        return forwardDelta;
    }

    public void setForwardDelta(double forwardDelta) {
        this.forwardDelta = forwardDelta;
    }

    public double getRightDelta() {
        return rightDelta;
    }

    public void setRightDelta(double rightDelta) {
        this.rightDelta = rightDelta;
    }

    public double getXPos() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getYPos() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
