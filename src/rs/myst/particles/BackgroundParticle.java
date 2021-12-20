package rs.myst.particles;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;
import mars.utils.Graphics;
import rs.myst.Particle;

// Single particle, part of the background starfield effect
public class BackgroundParticle extends Particle {
    private final double t;
    private final double duration;
    private final Vector p;
    private final double size;
    private final Color color;

    public BackgroundParticle(double t, double duration, Vector p, double size, Color color) {
        this.t = t;
        this.duration = duration;
        this.p = p;
        this.size = size;
        this.color = color;
    }

    @Override
    public void draw(View view, double time) {
        double td = time - t;
        double k = td / duration;

        view.setFill(Graphics.scaleOpacity(color, 1 - k));
        view.fillRectCentered(p.add(new Vector(td)), new Vector(k * size));
    }

    @Override
    public boolean isAlive(double time) {
        return time <= t + duration;
    }
}
