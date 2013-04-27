package net.zzorn.ld26.core.movers;

import com.badlogic.gdx.math.Vector3;
import net.zzorn.ld26.core.AspectBase;
import net.zzorn.ld26.core.Entity;
import net.zzorn.ld26.core.utils.MathUtils;

/**
 *
 */
public class AttachMover extends AspectBase {

    private Entity head;
    private float minDist = 10;
    private float factor = 10f;

    public AttachMover(Entity head) {
        this.head = head;
    }

    public AttachMover(Entity head, float minDist) {
        this.head = head;
        this.minDist = minDist;
    }

    @Override
    public void update(Entity entity, float deltaTime) {
        final Vector3 pos = entity.getPos();

        final Vector3 headPos = head.getPos();
        if (pos.dst(headPos) > minDist) {
            pos.x = MathUtils.mix(factor * deltaTime, pos.x, headPos.x);
            pos.y = MathUtils.mix(factor * deltaTime, pos.y, headPos.y);
            pos.z = MathUtils.mix(factor * deltaTime, pos.z, headPos.z);
        }


    }
}
