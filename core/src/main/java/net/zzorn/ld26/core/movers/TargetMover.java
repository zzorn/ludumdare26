package net.zzorn.ld26.core.movers;

import com.badlogic.gdx.math.Vector3;
import net.zzorn.ld26.core.AspectBase;
import net.zzorn.ld26.core.Entity;

import static net.zzorn.ld26.core.utils.MathUtils.*;

/**
 * Move towards target
 */
public class TargetMover extends AspectBase {

    private static final Vector3 delta = new Vector3();

    private Vector3 target;
    private float force = 1;
    private float forwardLook = 2;

    public TargetMover() {
        target = new Vector3();
    }

    public TargetMover(float x, float y, float z, final float force) {
        target = new Vector3(x, y, z);
        this.force = force;
    }

    public TargetMover(Vector3 target, final float force) {
        this.target = target;
        this.force = force;
    }

    @Override
    public void update(Entity entity, float deltaTime) {
        delta.set(entity.getVel());
        delta.scl(forwardLook);
        delta.add(entity.getPos());
        delta.scl(-1);
        delta.add(target);
        delta.clamp(0, 1);
        delta.scl(force);

        entity.getAcc().add(delta);
    }
}
