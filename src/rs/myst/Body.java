package rs.myst;

import mars.drawingx.drawing.View;
import mars.geometry.Vector;

public abstract class Body {
    /** Trenutak u kome je zabelezeno poslednje stanje (polozaj i brzina). */
    private double t;

    /** Polozaj tela. */
    private Vector p;

    /** Brzina tela. */
    private Vector v;

    /** Poluprecnik tela koji se koristi za proveru sudara. */
    private double r;

    public Body(double t, Vector p, Vector v, double r) {
        this.t = t;
        this.p = p;
        this.v = v;
        this.r = r;
    }

    public abstract void draw(View view, double time);

    private void update(double time) {
        p = positionAt(time);
        t = time;
    }

    public void setSpeed(Vector v, double time) {
        update(time);
        this.v = v;
    }

    public Vector positionAt(double time) {
        // Vraca polozaj u trenutku time.

        return p.add(v.mul(time - t));
    }

    public Vector velocityAt(double time) {
        return v;
    }

    public boolean collidesWith(Body o, double time) {
        // Vraca da li se ovo telo sece sa telom o u trenutku time.

        Vector p1 = positionAt(time);
        Vector p2 = o.positionAt(time);
        return p1.distanceTo(p2) <= r + o.r;
    }

    public double getR() {
        return r;
    }
}
