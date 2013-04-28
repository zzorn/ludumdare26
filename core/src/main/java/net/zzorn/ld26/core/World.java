package net.zzorn.ld26.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.zzorn.ld26.core.movers.AttachMover;
import net.zzorn.ld26.core.movers.RandomMover;
import net.zzorn.ld26.core.movers.TargetMover;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import static net.zzorn.ld26.core.utils.MathUtils.*;

/**
 *
 */
public class World {

    private static final float WORM_LUMINOSITY = 2f;
    private final TextureAtlas textureAtlas;

    private Array<Entity> entitiesToAdd = new Array<Entity>(100);
    private Array<Entity> entities = new Array<Entity>(1000);

    private Array<Entity> playerBullets = new Array<Entity>(100);
    private Array<Entity> enemyBullets = new Array<Entity>(100);
    private Array<Entity> enemies = new Array<Entity>(100);
    private Array<Entity> friendlies = new Array<Entity>(100);

    private Random random;
    private final Vector3 temp = new Vector3();
    private final Vector3 temp2 = new Vector3();

    private final Player player;

    public World(TextureAtlas textureAtlas, Player player) {
        this.textureAtlas = textureAtlas;
        this.player = player;
    }

    public Entity addEntity(Entity entity) {
        entitiesToAdd.add(entity);
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

        // Add entities
        for (Entity entity : entitiesToAdd) {
            doAddEntity(entity);
        }
        entitiesToAdd.clear();

        // Test collisions
        checkGroupCollisions(enemyBullets, friendlies);
        checkGroupCollisions(playerBullets, enemies);

        // Update
        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity entity = iterator.next();
            entity.update(deltaTime);

            // Remove entity if it got destroyed
            if (entity.isDestroyed()) {
                iterator.remove();
                enemyBullets.removeValue(entity, true);
                enemies.removeValue(entity, true);
                friendlies.removeValue(entity, true);
                playerBullets.removeValue(entity, true);
            }
        }
    }

    private void checkGroupCollisions(final Array<Entity> as, final Array<Entity> bs) {
        // Brute force ftw!
        for (Entity a : as) {
            for (Entity b : bs) {
                a.checkCollision(b);
            }
        }
    }

    public void render(PerspectiveCamera camera, DecalBatch decalBatch, TextureAtlas atlas) {

        // Render
        for (Entity entity : entities) {
            entity.render(camera, decalBatch, atlas);
        }
    }

    public void setup(long seed) {

        random = new Random(seed);

        createCloudLayer(0,350, 0, 1000);
        createCloudLayer(0,150, 0, 100);
        createCloudLayer(0,-150, 0, 100);
        createCloudLayer(0,-350, 0, 1000);

        for (int i = 0; i < 5; i++) {
            float x = (float) random.nextGaussian() * 500;
            float y = (float) random.nextGaussian() * 500;
            float z = (float) random.nextGaussian() * 1000;
            //createCloudLayer(x, y, z);
        }

        for (int i = 0; i < 20; i++) {
            float x = (float) random.nextGaussian() * 1000;
            float y = (float) random.nextGaussian() * 1000;
            float z = (float) random.nextGaussian() * 1000;

            float num = Math.abs((float) random.nextGaussian() * 6);
            createSwarm(x, y, z, 1000, num);
        }
    }

    private void createCloudLayer(final float baseX, final float baseY, final float baseZ, int num) {
        for (int i = 0; i < num; i++) {
            float x = baseX + (float) random.nextGaussian() * 1000;
            float y = baseY + (float) random.nextGaussian() * 50;
            float z = baseZ + (float) random.nextGaussian() * 1000f;
            float size = Math.abs((float) random.nextGaussian() * 10) + 100;

            String cloudName = "cloud" + (random.nextInt(4) + 1);

            final Entity cloud = addEntity(new Entity(x, y, z, size, cloudName))
                    .addAspect(
                            new RandomMover(
                                    randomVector(random, 2, 2, 2, temp),
                                    randomVector(random, 50, 50, 50, temp)))
                    .addAspect(
                            new TargetMover(x, y, z, 0.5f)
                    );
            cloud.setMaxThrust(500);

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
            createWorm(tx, ty, tz, size, "greenball", random.nextInt(15)+10).
                    addAspect(new TargetMover(queen.getPos(), 1));
        }

        return queen;
    }

    private Entity createWorm(float x, float y, float z, float size, String sprite, int len) {
        Color color = new Color(mix(random.nextFloat(), 0.5f, 1),
                                mix(random.nextFloat(), 0.5f, 1),
                                mix(random.nextFloat(), 0.5f, 1),
                                1);

        final Entity head = addEntity(new Entity(x, y, z, size, sprite))
                .addAspect(
                        new RandomMover(
                                randomVector(random, 10f, 10f, 10f, temp),
                                randomVector(random, 2, 2, 2, temp)))
                .addAspect(
                        new TargetMover(x, y, z, 0.5f)
                );

        head.setLuminosity(WORM_LUMINOSITY);
        head.setColor(color);
        head.setCollisionGroup(CollisionGroup.ENEMIES);

        Entity prev = head;
        float s = size * 0.8f;
        for (int i = 0; i < len; i++) {

            Entity tail = addEntity(new Entity(x, y, z, s, sprite))
                    .addAspect(new AttachMover(prev, size / 10));

            tail.setLuminosity(WORM_LUMINOSITY);
            tail.setColor(color);

            prev = tail;
            s *= 0.9;
        }

        head.setMaxThrust(100000);

        return head;
    }

    private void doAddEntity(Entity entity) {
        entities.add(entity);

        if (entity.getCollisionGroup() != null) {
            switch (entity.getCollisionGroup()) {
                case FRIENDLIES:
                    friendlies.add(entity);
                    break;
                case FRIENDLY_BULLETS:
                    playerBullets.add(entity);
                    break;
                case ENEMIES:
                    enemies.add(entity);
                    break;
                case ENEMY_BULLETS:
                    enemyBullets.add(entity);
                    break;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
