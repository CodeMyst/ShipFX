package rs.myst.particles;

import mars.geometry.Vector;
import mars.random.sampling.Sampling;
import rs.myst.Asteroid;

// Explosion particle (when asteroid destroyed)
public class ExplosionParticleSystem extends ParticleSystemInstant<ExplosionParticle> {
    private Asteroid enemy;

    public ExplosionParticleSystem(double t, Asteroid enemy) {
        // instantly spawn 300 particles
        super(t, 300);
        this.enemy = enemy;
    }

    @Override
    protected ExplosionParticle generateNext() {
        double t = timeNext();

        return new ExplosionParticle(
                t,
                Math.max(0, 0.4 * (0.7 + Sampling.exponential(0.3))),
                enemy.positionAt(t).add(Vector.randomInDisk(enemy.getR())),
                enemy.velocityAt(t).add(Vector.randomOnCircle(150).mul(Sampling.gaussian(1, 0.3)))
        );
    }
}
