package rs.myst;

import javafx.scene.image.Image;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;
import rs.myst.particles.ProjectileParticleSystem;

public class Projectile extends Body {
    private Image sprite;

    private ProjectileParticleSystem ps;

    private boolean finished = false;

    public Projectile(Ship ship, double time, Image sprite) {
        super(
                time,
                ship.positionAt(time).add(new Vector(0, ship.getR())),
                new Vector(0, 400),
                2);

        this.sprite = sprite;

        ps = new ProjectileParticleSystem(time, this);
    }

    public void finish() {
        if (!finished) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void draw(View view, double time) {
        Vector p = positionAt(time);

        ps.draw(view, time);

        view.setImageSmoothing(false);
        view.drawImageCentered(p, sprite, 0.75);
    }
}
