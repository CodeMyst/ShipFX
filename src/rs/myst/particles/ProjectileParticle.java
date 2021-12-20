package rs.myst.particles;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;
import rs.myst.Particle;

public class ProjectileParticle extends Particle {
    private double t0, tD;
    private Vector p, v;
    private double brightness;

    public ProjectileParticle(double t0, double tD, Vector p, Vector v) {
        this.t0 = t0;
        this.tD = tD;
        this.p = p;
        this.v = v;
        brightness = 0.5 + 0.5 * Math.random();
    }

    @Override
    public void draw(View view, double time) {
        double t = time - t0;
        if (t < 0) {
            return;
        }

        double k = t / tD;
        view.setFill(Color.hsb(
                0,
                0.5,
                brightness,
                0.6 * (1 - k)
            ));
        Vector q = p.add(v.mul(t));
        view.fillRectCentered(q, new Vector(2.5));
    }

    @Override
    public boolean isAlive(double time) {
        return time - t0 < tD;
    }
}
