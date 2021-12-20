package rs.myst;

import javafx.scene.paint.Color;
import mars.drawingx.drawing.DrawingUtils;
import mars.drawingx.drawing.View;
import rs.myst.particles.BackgroundParticleSystem;

public class Background {
    private BackgroundParticleSystem ps;

    public Background() {
        ps = new BackgroundParticleSystem();
    }

    public void draw(View view, double time) {
        DrawingUtils.clear(view, Color.hsb(0, 0, 0.05));
        ps.draw(view, time);
    }
}
