package rs.myst;

import javafx.scene.image.Image;
import mars.drawingx.drawing.View;
import mars.geometry.Transformation;
import mars.geometry.Vector;

public class Asteroid extends Body {
    private Image sprite;

    // random rotation direction
    private boolean dir = Math.random() < 0.5;

    public Asteroid(double time, Image sprite) {
        super(
                time,
                new Vector(-350 + 700 * Math.random(), 425),
                new Vector(0, (-30 - 30 * Math.random()) * (1 + time / 20)),
                15);

        this.sprite = sprite;
    }

    @Override
    public void draw(View view, double time) {
        Vector p = positionAt(time);

        view.stateStore();

        // rotate the asteroid
        Transformation t = new Transformation().rotate(0.1 * time * (dir ? -1 : 1)).translate(p);
        view.setTransformation(t);
        view.setImageSmoothing(false);
        view.drawImageCentered(Vector.ZERO, sprite, 0.45);

        view.stateRestore();
    }
}
