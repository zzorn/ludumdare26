package net.zzorn.ld26.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

/**
 *
 */
public abstract class AspectBase implements Aspect {
    @Override
    public void update(Entity entity, float deltaTime) {
    }

    @Override
    public void render(Entity entity, Vector3 screenPos, float size, SpriteBatch spriteBatch, TextureAtlas atlas) {
    }
}
