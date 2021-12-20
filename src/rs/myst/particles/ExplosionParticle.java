package rs.myst.particles;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;
import mars.random.sampling.Sampling;
import rs.myst.Particle;

// Single explosion particle.
public class ExplosionParticle extends Particle {
    private double t0, tD;
    private Vector p, v;
    private double hue;

    public ExplosionParticle(double t0, double tD, Vector p, Vector v) {
        this.t0 = t0;
        this.tD = tD;
        this.p = p;
        this.v = v;
        hue = Sampling.gaussian(40, 5);
    }

    @Override
    public void draw(View view, double time) {
        double t = time - t0;
        if (t < 0) {
            return;
        }

        double k = t / tD;
        view.setFill(Color.hsb(
                hue,
                0.3 + 0.5 * k,
                1,
                1 - k*k*k
            ));
        Vector q = p.add(v.mul(t));
        view.fillRectCentered(q, new Vector(3));
    }

    @Override
    public boolean isAlive(double time) {
        return time - t0 < tD;
    }
}
