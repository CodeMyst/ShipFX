package rs.myst.particles;

import rs.myst.Particle;

public abstract class ParticleSystemInterval<P extends Particle> extends ParticleSystem<P> {
    private double interval;
    private double timeNext;

    public ParticleSystemInterval(double timeStart, double interval) {
        this.timeNext = timeStart;
        this.interval = interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public void stop() {
        setInterval(Double.POSITIVE_INFINITY);
    }

    @Override
    protected double advance() {
        return timeNext += interval;
    }
}
