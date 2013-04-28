package net.zzorn.ld26.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static net.zzorn.ld26.core.utils.MathUtils.*;

/**
 *
 */
public class Entity {

    private Array<Aspect> aspects = new Array<Aspect>();

    private Weapon weapon;

    private CollisionGroup collisionGroup = null;

    private static final Vector3 temp1 = new Vector3();
    private static final Vector3 temp2 = new Vector3();
    private static final float SQRT_2 = (float) Math.sqrt(2);

    private final String textureName;
    private World world;

    private Vector3 pos = new Vector3();
    private Vector3 vel = new Vector3();
    private Vector3 acc = new Vector3();
    private Vector3 direction = new Vector3(0, 0, 1);
    private Vector3 screenPos = new Vector3();
    private float screenDistance = 0;

    private Color color = new Color(1, 1, 1, 1);
    private float luminosity = 1;

    private float maxVel = 200;
    private float maxThrust = 10000;
    private float size = 100;
    private float mass = 100;

    private boolean destroyed = false;

    private float screenSize;
    private float health = 100;
    private float damage = 10;
    private float lifeTimeLeft = 0;
    private boolean expiresAfterTime = false;

    private TextureRegion textureRegion;

    private float turnSpeed = 1;
    private float forwardSpeed = 200;
    private float strafeSpeed = 200;

    public Entity(float x, float y, float z, float size, String textureName) {
        this.textureName = textureName;

        pos.set(x, y, z);
        this.size = size;
    }

    public Entity addAspect(Aspect aspect) {
        aspects.add(aspect);
        return this;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        textureRegion = world.getTextureAtlas().findRegion(textureName);
    }

    public void update(float deltaTime) {

        if (weapon != null) weapon.update(deltaTime);

        // Update aspects
        for (Aspect aspect : aspects) {
            aspect.update(this, deltaTime);
        }

        // Scale control acceleration with thrust produced
        acc.scl(maxThrust);
        acc.clamp(0, maxThrust);

        // Apply air resistance to acceleration.
        final float airResistance = world.getAirResistance(pos);
        float speed = vel.len();
        float dragForce = airResistance * speed;
        acc.add(-vel.x * dragForce);
        acc.add(-vel.y * dragForce);
        acc.add(-vel.z * dragForce);

        // Apply acceleration
        mulAdd(vel, acc, deltaTime / mass);

        // Clamp to terminal velocity
        vel.clamp(0, maxVel);

        // Apply velocity
        mulAdd(pos, vel, deltaTime);

        // Zero acceleration (it is set again from user input or AI movement)
        acc.set(0,0,0);

        // Check for death
        if (expiresAfterTime) {
            lifeTimeLeft -= deltaTime;
            if (lifeTimeLeft < 0) {
                setDestroyed(true);
            }
        }
    }

    public void render(SpriteBatch spriteBatch, TextureAtlas atlas) {
        if (screenDistance > 1) {
            float f = map(screenDistance, 0, 5000, 1, 0);
            f = f * f;
            f = clamp0to1(f);
            spriteBatch.setColor(clamp0to1(color.r * f * luminosity),
                                 clamp0to1(color.g * f * luminosity),
                                 clamp0to1(color.b * f * luminosity),
                                 1);

            spriteBatch.draw(textureRegion, screenPos.x - screenSize / 2, screenPos.y - screenSize / 2, screenSize, screenSize);

            // Render aspects
            for (Aspect aspect : aspects) {
                aspect.render(this, screenPos, screenSize, spriteBatch, atlas);
            }
        }
    }

    public void project(PerspectiveCamera camera) {
        screenPos.set(pos);
        temp2.set(pos);
        temp2.add(size, size, 0);
        camera.project(screenPos);
        camera.project(temp2);

        screenSize = temp2.dst(screenPos) / SQRT_2;
        screenDistance = pos.dst(camera.position);
    }


    public Vector3 getAcc() {
        return acc;
    }

    public Vector3 getVel() {
        return vel;
    }

    public Vector3 getPos() {
        return pos;
    }

    public float getMaxThrust() {
        return maxThrust;
    }

    public void setMaxThrust(float maxThrust) {
        this.maxThrust = maxThrust;
    }

    public Vector3 getScreenPos() {
        return screenPos;
    }

    public float getScreenDistance() {
        return screenDistance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(float luminosity) {
        this.luminosity = luminosity;
    }

    public final boolean overlaps(Entity other) {
        float threshold = size*0.5f + other.size*0.5f;
        return pos.dst2(other.getPos()) <= threshold * threshold;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public final void checkCollision(Entity other) {
        if (overlaps(other)) {
            onCollision(other);
            other.onCollision(this);
        }
    }

    /**
     * Called when the entity collided with something else
     */
    protected void onCollision(Entity other) {
        other.doDamage(damage);
    }

    public void doDamage(float amount) {
        health -= damage;
        if (health <= 0) {
            setDestroyed(true);

            // TODO: Spawn death animation
        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void shoot() {
        if (weapon != null) weapon.shoot(this);
    }

    public void setExpiration(float lifeTime) {
        lifeTimeLeft = lifeTime;
        expiresAfterTime = true;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public CollisionGroup getCollisionGroup() {
        return collisionGroup;
    }

    public void setCollisionGroup(CollisionGroup collisionGroup) {
        this.collisionGroup = collisionGroup;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Vector3 getDirection() {
        return direction;
    }
}
