package rs.myst;

import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import mars.drawingx.application.DrawingApplication;
import mars.drawingx.application.Options;
import mars.drawingx.application.parameters.WindowState;
import mars.drawingx.drawing.Drawing;
import mars.drawingx.drawing.View;
import mars.drawingx.gadgets.annotations.GadgetAnimation;
import mars.geometry.Vector;
import mars.input.InputEvent;
import mars.input.InputState;
import rs.myst.particles.ExplosionParticleSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Game implements Drawing {
    private final double TIME_TICK_INTERVAL = 0.001;
    private final double TIME_ENEMY_INTERVAL = 1.0;

    private Image shipSprite;
    private Image asteroidSprite;
    private Image projectileSprite;

    private Media shootSound;
    private Media explodeSound;
    private Media backgroundMusic;

    private MediaPlayer shootPlayer;
    private MediaPlayer explodePlayer;
    private MediaPlayer backgroundPlayer;

    Font FONT_SCORE;
    Font FONT_GAMEOVER;

    @GadgetAnimation(start = true)
    double time = 0.0;

    Background background;
    Ship ship;
    Set<Asteroid> enemies;
    Set<Projectile> projectiles;
    Set<ExplosionParticleSystem> explosions;

    double timeLastUpdate;
    double timeNextEnemy;
    int projectilesRemaining;
    int enemiesDestroyed;
    boolean gameOver;

    void reset() {
        time = 0.0;
        background = new Background();
        ship = new Ship(time, shipSprite);
        enemies = new HashSet<>();
        projectiles = new HashSet<>();
        explosions = new HashSet<>();
        timeLastUpdate = 0;
        timeNextEnemy = 0;
        projectilesRemaining = 10;
        enemiesDestroyed = 0;
        gameOver = false;
    }

    @Override
    public void init(View view) {
        try {
            FONT_SCORE = Font.loadFont(new FileInputStream("assets/prstart.ttf"), 50);
            FONT_GAMEOVER = Font.loadFont(new FileInputStream("assets/prstart.ttf"), 70);

            shipSprite = new Image(new FileInputStream("assets/ship.png"));
            asteroidSprite = new Image(new FileInputStream("assets/asteroid.png"));
            projectileSprite = new Image(new FileInputStream("assets/laser.png"));

            shootSound = new Media(new File("assets/Shoot.wav").toURI().toString());
            explodeSound = new Media(new File("assets/Explode.wav").toURI().toString());
            backgroundMusic = new Media(new File("assets/DefriniSpookie.wav").toURI().toString());
        } catch (FileNotFoundException e) {}

        shootPlayer = new MediaPlayer(shootSound);
        shootPlayer.setVolume(0.25);

        explodePlayer = new MediaPlayer(explodeSound);
        explodePlayer.setVolume(0.5);

        backgroundPlayer = new MediaPlayer(backgroundMusic);
        backgroundPlayer.setVolume(0.15);
        backgroundPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundPlayer.seek(Duration.ZERO);
            }
        });

        backgroundPlayer.play();

        reset();
    }

    void update(double t) {
        // Azuriranje stanja u trenutku t.

        // Dodavanje novih neprijateljskih brodova.
        while (timeNextEnemy <= t) {
            enemies.add(new Asteroid(t, asteroidSprite));
            timeNextEnemy += TIME_ENEMY_INTERVAL;
        }

        // Uklanjanje stvari koje nam vise ne trebaju.

        explosions.removeIf(e -> !e.isAlive());
        projectiles.removeIf(p -> p.isFinished());

        Set<Asteroid> enemiesToRemove = new HashSet<>();

        for (Projectile projectile : projectiles) {
            if (!projectile.isFinished() && projectile.positionAt(t).y > 500) {
                projectile.finish();
            }
        }

        for (Asteroid enemy : enemies) {
            // Uklanjamo protivnika ako se spustio ispod vidnog polja.
            if (enemy.positionAt(t).y < -450) {
                enemiesToRemove.add(enemy);
                addProjectiles(-1);
            }

            for (Projectile projectile : projectiles) {
                // Proveravanje za svaki par (enemy, projectile) da li je doslo do sudara. Ako
                // jeste,
                // pravimo eksploziju na mestu neprijateljskog broda i uklanjamo i njega i
                // projektil.
                if (!projectile.isFinished()) {
                    if (enemy.collidesWith(projectile, t)) {
                        explosions.add(new ExplosionParticleSystem(t, enemy));
                        enemiesToRemove.add(enemy);
                        projectile.finish();
                        addProjectiles(1);
                        enemiesDestroyed++;
                        explodePlayer.seek(Duration.ZERO);
                        explodePlayer.play();
                    }
                }
            }
        }

        enemies.removeAll(enemiesToRemove);

        // Proveravamo da li je game over.
        checkGameOver();
    }

    void checkGameOver() {
        if (projectilesRemaining <= 0 && projectiles.isEmpty()) {
            gameOver = true;
        }
    }

    void addProjectiles(int change) {
        projectilesRemaining += change;
    }

    void fireProjectile() {
        if (projectilesRemaining > 0) {
            projectiles.add(new Projectile(ship, time, projectileSprite));
            addProjectiles(-1);
            shootPlayer.seek(Duration.ZERO);
            shootPlayer.play();
        }
    }

    @Override
    public void draw(View view) {
        // Azuriranje stanja, tick po tick.

        while (timeLastUpdate + TIME_TICK_INTERVAL <= time) {
            timeLastUpdate += TIME_TICK_INTERVAL;
            update(timeLastUpdate);
        }

        // Iscrtavanje.

        view.stateStore();

        view.setUsingWorldSpace(true); // Crtamo u "svetu igre", koordinatni pocetak je u centru prozora.
        background.draw(view, time);

        for (Asteroid enemy : enemies) {
            enemy.draw(view, time);
        }

        ship.draw(view, time);

        for (Projectile projectile : projectiles) {
            projectile.draw(view, time);
        }

        for (ExplosionParticleSystem explosion : explosions) {
            explosion.draw(view, time);
        }

        view.setUsingWorldSpace(false); // Crtamo po "displeju".

        view.setFill(Color.hsb(0, 0, 1, 1));
        Vector p = new Vector(16, 16);
        Vector d = new Vector(10, 30);
        Vector l = new Vector(20, 0);
        for (int i = 0; i < projectilesRemaining; i++) {
            view.fillRect(p.add(l.mul(i)), d);
        }

        view.setFont(FONT_SCORE);
        view.setTextAlign(TextAlignment.RIGHT);
        view.setTextBaseline(VPos.BASELINE);
        view.fillText("" + enemiesDestroyed, view.getCornerLowerRight().add(new Vector(-p.x, p.y)));

        if (gameOver) {
            view.setFont(FONT_GAMEOVER);
            view.setFill(Color.hsb(0, 0, 1));
            view.setTextAlign(TextAlignment.CENTER);
            view.setTextBaseline(VPos.CENTER);
            view.fillText("Game over", view.getCenter());
        }

        view.stateRestore();
    }

    @Override
    public void receiveEvent(View view, InputEvent event, InputState state, Vector pointerWorld,
            Vector pointerViewBase) {
        if (event.isKey(KeyCode.LEFT) || event.isKey(KeyCode.RIGHT)) {
            int dir = 0;
            dir += state.keyPressed(KeyCode.LEFT) ? -1 : 0;
            dir += state.keyPressed(KeyCode.RIGHT) ? 1 : 0;
            ship.setSpeed(Vector.UNIT_X.mul(dir * 300), time);
        }
        if (event.isKeyPress(KeyCode.SPACE)) {
            fireProjectile();
        }
        if (event.isKeyPress(KeyCode.ENTER)) {
            reset();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();

        options.constructGui = false;
        options.hideMouseCursor = false;
        options.drawingSize = new Vector(800, 800);
        options.resizable = false;
        options.initialWindowState = WindowState.NORMAL;

        DrawingApplication.launch(options);
    }
}
