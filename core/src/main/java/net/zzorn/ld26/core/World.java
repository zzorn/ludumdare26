package net.zzorn.ld26.core;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.zzorn.ld26.core.movers.AttachMover;
import net.zzorn.ld26.core.movers.RandomMover;
import net.zzorn.ld26.core.movers.TargetMover;

import java.util.Comparator;
import java.util.Random;

import static net.zzorn.ld26.core.utils.MathUtils.*;

/**
 *
 */
public class World {

    private final TextureAtlas textureAtlas;

    private Array<Entity> entities = new Array<Entity>(1000);
    private Random random;
    private final Vector3 temp = new Vector3();
    private final Vector3 temp2 = new Vector3();

    public World(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public Entity addEntity(Entity entity) {
        entities.add(entity);
        entity.setWorld(this);
        return entity;
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public float getAirResistance(Vector3 pos) {
        return mapClamp(pos.z, -20, 20, 0.01f, 0.97f);
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }

    public void render(PerspectiveCamera camera, SpriteBatch spriteBatch, TextureAtlas atlas) {

        // Project
        for (Entity entity : entities) {
            entity.project(camera);
        }

        // Sort by depth
        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                final float z1 = o1.getScreenPos().z;
                final float z2 = o2.getScreenPos().z;

                if (z1 > z2) return -1;
                else if (z1 < z2) return 1;
                else return 0;
            }
        });

        // Render
        for (Entity entity : entities) {
            entity.render(spriteBatch, atlas);
        }
    }

    public void setup(long seed) {

        random = new Random(seed);

        for (int i = 0; i < 1000; i++) {
            float x = (float) random.nextGaussian() * 1000;
            float y = (float) random.nextGaussian() * 1000;
            float z = (float) random.nextGaussian() * 1000;
            float size = Math.abs((float) random.nextGaussian() * 10) + 100;
            final Entity cloud = addEntity(new Entity(x, y, z, size, "redbox"))
                    .addAspect(
                            new RandomMover(
                                    randomVector(random, 1, 1, 1, temp),
                                    randomVector(random, 50, 50, 50, temp)))
                    .addAspect(
                            new TargetMover(x, y, z, 0.001f)
                    );
            cloud.setMaxThrust(100);

        }

        for (int i = 0; i < 10; i++) {
            float x = (float) random.nextGaussian() * 1000;
            float y = (float) random.nextGaussian() * 1000;
            float z = (float) random.nextGaussian() * 1000;

            float num = Math.abs((float) random.nextGaussian() * 10);
            createSwarm(x, y, z, 1000, num);
        }
    }

    private Entity createSwarm(float x, float y, float z, float spread, float num) {

        // Queen
        final Entity queen = createWorm(x, y, z, 100, "greenball", random.nextInt(20) + 5)
                .addAspect(new RandomMover());

        for (int i = 0; i < num; i++) {
            float tx = x + (float) random.nextGaussian() * spread;
            float ty = y + (float) random.nextGaussian() * spread;
            float tz = z + (float) random.nextGaussian() * spread;
            float size = Math.abs((float) random.nextGaussian() * 10) + 50;
            createWorm(tx, ty, tz, size, "greenball", random.nextInt(10)+5).
                    addAspect(new TargetMover(queen.getPos(), 1));
        }

        return queen;
    }

    private Entity createWorm(float x, float y, float z, float size, String sprite, int len) {
        final Entity head = addEntity(new Entity(x, y, z, size, sprite))
                .addAspect(
                        new RandomMover(
                                randomVector(random, 10f, 10f, 10f, temp),
                                randomVector(random, 2, 2, 2, temp)))
                .addAspect(
                        new TargetMover(x, y, z, 0.5f)
                );

        Entity prev = head;
        float s = size * 0.8f;
        for (int i = 0; i < len; i++) {

            Entity tail = addEntity(new Entity(x, y, z, s, sprite))
                    .addAspect(new AttachMover(prev, size / 10));
            prev = tail;
            s *= 0.9;
        }

        head.setMaxThrust(100000);

        return head;
    }
}