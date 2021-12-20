package rs.myst.particles;

import mars.geometry.Vector;
import mars.random.sampling.Sampling;
import rs.myst.Projectile;

public class ProjectileParticleSystem extends ParticleSystemInterval<ProjectileParticle> {
    public static double INTERVAL = 0.002;

    private Projectile projectile;

    public ProjectileParticleSystem(double t, Projectile projectile) {
        super(t, INTERVAL);
        this.projectile = projectile;
    }

    @Override
    protected ProjectileParticle generateNext() {
        double time = timeNext();
        Vector p = projectile.positionAt(time);
        Vector v = new Vector(0, -50);

        return new ProjectileParticle(
                time,
                0.4 * (0.7 + Sampling.exponential(0.3)),
                p,
                v.add(Vector.randomGaussian(40)).mul(0.3 + 0.4 * Sampling.uniform())
        );
    }
}
