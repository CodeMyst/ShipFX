package rs.myst;

import javafx.scene.image.Image;
import mars.drawingx.drawing.View;
import mars.geometry.Vector;
import rs.myst.particles.ShipParticleSystem;

public class Ship extends Body {
    private Image sprite;

    private ShipParticleSystem ps;

    public Ship(double time, Image sprite) {
        super(0, new Vector(0, -300), Vector.ZERO, 20);

        this.sprite = sprite;

        ps = new ShipParticleSystem(time, this);
    }

    @Override
    public void draw(View view, double time) {
        Vector p = positionAt(time);

        ps.draw(view, time);

        view.setImageSmoothing(false);
        view.drawImageCentered(p, sprite, 0.75);
    }
}
