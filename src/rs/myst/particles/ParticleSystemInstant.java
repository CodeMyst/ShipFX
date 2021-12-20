package rs.myst.particles;

import rs.myst.Particle;

public abstract class ParticleSystemInstant<P extends Particle> extends ParticleSystem<P> {
    protected double timeGeneration;
    int nGenerated = 0;
    int count;

    public ParticleSystemInstant(double timeGeneration, int count) {
        this.timeGeneration = timeGeneration;
        this.count = count;
    }

    @Override
    protected double advance() {
        return (nGenerated++ < count ? timeGeneration : Double.POSITIVE_INFINITY);
    }
}
