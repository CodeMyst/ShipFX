package rs.myst.particles;

import javafx.scene.paint.Color;
import mars.geometry.Vector;

// Particle system for the background. Starfield effect.
public class BackgroundParticleSystem extends ParticleSystemInterval<BackgroundParticle> {
    public static double INTERVAL = 0.001;

    public BackgroundParticleSystem() {
        super(-10, INTERVAL);
    }

    @Override
    protected BackgroundParticle generateNext() {
        double time = timeNext();
        return new BackgroundParticle(
                // start time
                time,
                // duration
                1,
                // random pos inside screen
                Vector.randomInBox(new Vector(-400, -400), new Vector(800, 800)),
                // random size
                3 * Math.random(),
                // random color
                Color.hsb(360 * Math.random(), 0.3, 0.6 + 0.4 * Math.random()));
    }
}
