package rs.myst.particles;

import mars.drawingx.drawing.View;
import rs.myst.Particle;

import java.util.ArrayList;
import java.util.List;

/**
 * Time-monotone.
 */
public abstract class ParticleSystem<P extends Particle> {
    /** Generates the next particle at the moment timeNext(). */
    protected abstract P generateNext();

    /**
     * Called after generateNext, to update whatever is needed.
     * Returns the time when the next particle should be generated. If there will be
     * no more particles, it should
     * return Double.PositiveInfinity.
     */
    protected abstract double advance();

    protected List<P> particles = new ArrayList<P>();
    private double timeNext = Double.NEGATIVE_INFINITY;

    public void update(double timeTo) {
        if (timeNext == Double.NEGATIVE_INFINITY) {
            timeNext = advance();
        }

        while (timeNext <= timeTo) {
            particles.add(generateNext());
            timeNext = advance();
        }
        particles.removeIf(p -> !p.isAlive(timeTo));
    }

    public void draw(View view, double time) {
        update(time);

        for (Particle particle : particles) {
            particle.draw(view, time);
        }
    }

    public boolean isAlive() {
        return (!particles.isEmpty()) || (timeNext < Double.POSITIVE_INFINITY);
    }

    protected double timeNext() {
        return timeNext;
    }
}
