package rs.myst;

import mars.drawingx.drawing.View;

public abstract class Particle {
    public abstract void draw(View view, double time);

    public abstract boolean isAlive(double time);
}
