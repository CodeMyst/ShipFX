package rs.myst.particles;

import mars.geometry.Vector;
import mars.random.sampling.Sampling;
import rs.myst.Ship;

public class ShipParticleSystem extends ParticleSystemInterval<ShipParticle> {
    public static double INTERVAL = 0.002;

    private Ship ship;

    public ShipParticleSystem(double t, Ship ship) {
        super(t, INTERVAL);
        this.ship = ship;
    }

    @Override
    protected ShipParticle generateNext() {
        double time = timeNext();
        Vector p = ship.positionAt(time).add(new Vector(0, -20));
        Vector v = new Vector(0, -400);

        return new ShipParticle(
                time,
                0.4 * (0.7 + Sampling.exponential(0.3)),
                p,
                v.add(Vector.randomGaussian(80)).mul(0.4 + 0.5 * Sampling.uniform()));
    }
}
