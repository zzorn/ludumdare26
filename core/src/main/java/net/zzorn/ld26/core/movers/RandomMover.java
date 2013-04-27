package net.zzorn.ld26.core.movers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import net.zzorn.ld26.core.AspectBase;
import net.zzorn.ld26.core.Entity;
import net.zzorn.ld26.core.utils.SimplexGradientNoise;

/**
 * Moves entity around in oscillating pattern
 */
public class RandomMover extends AspectBase {

    private double t = Math.random() * 100;

    private Vector3 amplitude = new Vector3(100, 10, 100);
    private Vector3 wavelength = new Vector3(10, 3, 10);

    public RandomMover() {
    }

    public RandomMover(Vector3 amplitude, Vector3 wavelength) {
        this.amplitude = amplitude;
        this.wavelength = wavelength;
    }

    public RandomMover(float amplitude, float wavelength) {
        this.amplitude.set(amplitude, amplitude, amplitude);
        this.wavelength.set(wavelength, wavelength, wavelength);
    }

    public RandomMover(float amplitudeX, float amplitudeY, float amplitudeZ,
                       float wavelengthX, float wavelengthY, float wavelengthZ) {
        this.amplitude.set(amplitudeX, amplitudeY, amplitudeZ);
        this.wavelength.set(wavelengthX, wavelengthY, wavelengthZ);
    }

    @Override
    public void update(Entity entity, float deltaTime) {
        t += deltaTime;
        float dx = amplitude.x * (float) (SimplexGradientNoise.sdnoise1(t / wavelength.x + 13.123));
        float dy = amplitude.y * (float) (SimplexGradientNoise.sdnoise1(t / wavelength.y + 312.412));
        float dz = amplitude.z * (float) (SimplexGradientNoise.sdnoise1(t / wavelength.z + 123.312));

        entity.getAcc().add(dx, dy, dz);
    }

}
