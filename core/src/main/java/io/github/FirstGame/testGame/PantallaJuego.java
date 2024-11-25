package io.github.FirstGame.testGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;    
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXAsteroides; 
    private int cantAsteroides;
    private boolean powerUpActive = false;
    private int powerUpDuration = 300;
    private ShootingStrategy defaultStrategy;
    private float tiempoUltimoPowerUp = 0; // Tiempo desde el último power-up
    private final float intervaloPowerUp = 10f; // Intervalo en segundos

    private Texture gameFondo;
    
    private Nave4 nave;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();
    private ArrayList<Bullet> balasEnemy = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    // Matriz de enemigos
    private MatrizBall2 asteroides;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int velXAsteroides, int cantAsteroides) {
        this.game = SpaceNavigation.getInstance();
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.cantAsteroides = cantAsteroides;
        defaultStrategy = new SimpleShootingStrategy();
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        
        gameFondo = new Texture(Gdx.files.internal("GameFondo.png"));
        
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.mp3")); 
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        
        nave = new Nave4(740/2 - 50, 30,
                new Texture(Gdx.files.internal("playerShip_1.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        nave.setShootingStrategy(defaultStrategy);
        
        crearAsteroides();
    }

    private void crearAsteroides() {
        int columnas = 10; // Número de columnas
        int filas = 3; // Número de filas
        int espacioEntreBolas = 20; // Espacio entre las bolas
        int size = 50; // Tamaño de las bolas
        
        asteroides = new MatrizBall2(filas, columnas, espacioEntreBolas, size);
        
        int velocidadConstante = velXAsteroides; // Velocidad constante para todas las bolas
        
        asteroides.CrearEnemigos(enemies, velocidadConstante);
    }

    public void dibujaEncabezado() {
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, "" + this.score, 910, 800 - 76);
        game.getFont().draw(batch, "" + game.getHighScore(), 910, 800 - 41);
        game.getFont().draw(batch, "" + nave.getVidas(), 860, 800 - 280);
        game.getFont().draw(batch, "" + ronda, 860, 800 - 315);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(gameFondo, 0, 0, 1200, 800);
        dibujaEncabezado();
        
        if (powerUpActive) {
            game.getFont().draw(batch, "PowerUp: " + powerUpDuration, 50, 750);
        }
        
        drawPowerUps(batch); // Dibujar los power-ups
        updatePowerUps(); // Actualizar posición de los power-ups
        checkColisionPowerUp(); // Verificar si la nave recoge algún power-up
        
        if (powerUpActive) {
            if (powerUpDuration > 0) {
                powerUpDuration--;
            } else {
                nave.setShootingStrategy(defaultStrategy);
                powerUpActive = false;
            }
        }
        
        tiempoUltimoPowerUp += delta; // Incrementar el tiempo acumulado

        if (tiempoUltimoPowerUp >= intervaloPowerUp) {
            generarPowerUps(); // Generar un power-up
            tiempoUltimoPowerUp = 0; // Reiniciar el temporizador
        }

        if (!nave.estaHerido()) {
            asteroides.initEnemy(this);
            nave.init(this);
            checkColisionMiBalaConEnemy();
            checkColisionEnemyBalaConNave();
        }
        
        drawBalas();
        drawNave();
        
        drawEnemyAndCheckColision();
        
        // Aquí invocamos el método `action()` para la nave
        nave.action(batch, this);

        // Aquí invocamos el método `action()` para cada enemigo
        for (Enemy enemy : enemies) {
            enemy.action(batch, this);
        }
        
        if (nave.estaDestruido()) {
            callGameOver();
        }
        
        batch.end();
        
        // Nivel completado
        if (enemies.size() == 0) {
            nextLevel();
        }
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }
    
    public boolean agregarBalaEnemy(Bullet bb) {
        return balasEnemy.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        Preferences prefs = Gdx.app.getPreferences("GameState");
        prefs.putInteger("score", score);
        prefs.putInteger("ronda", ronda);
        prefs.putInteger("vidas", nave.getVidas());
        prefs.putInteger("cantAsteroides", cantAsteroides);
        prefs.putFloat("velXAsteroides", velXAsteroides);
        prefs.putInteger("asteroidesCount", enemies.size());
        
        // Guardar el estado de cada asteroide
        for (int i = 0; i < enemies.size(); i++) {
            Ball2 asteroide = (Ball2) enemies.get(i);
            prefs.putFloat("asteroide_" + i + "_x", asteroide.getX());
            prefs.putFloat("asteroide_" + i + "_y", asteroide.getY());
            prefs.putFloat("asteroide_" + i + "_velX", asteroide.getXSpeed());
        }
        prefs.flush(); // Guarda los cambios
    }

    public void checkColisionMiBalaConEnemy() {
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            for (int fila = 0; fila < asteroides.getFila(); fila++) {
                for (int col = 0; col < asteroides.getColumna(); col++) {
                    if (asteroides.getElement(fila, col) != null && b.checkCollision(asteroides.getElement(fila, col))) {
                        explosionSound.play();
                        enemies.remove(asteroides.getElement(fila, col));
                        asteroides.delete(fila, col);
                        score += 10;
                    }
                }
            }

            if (b.isDestroyed()) {
                balas.remove(i);
                i--; // Ajustar índice tras eliminación
            }
        }
    }

    public void checkColisionEnemyBalaConNave() {
        for (int i = 0; i < balasEnemy.size(); i++) {
            Bullet b = balasEnemy.get(i);
            b.update2();
            b.checkCollision(nave);
            nave.checkCollision(b);
            if (b.isDestroyed()) {
                balasEnemy.remove(i);
                i--; // Para no saltarse uno tras eliminar del ArrayList
            }
        }
    }

    public void drawBalas() {
        // Dibujar balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        
        // Dibujar balas de enemigos
        for (Bullet b : balasEnemy) {
            b.draw2(batch);
        }
    }

    public void drawNave() {
        nave.draw(batch, this);
    }

    public void drawEnemyAndCheckColision() {
        // Dibujar enemigos y manejar colisión con nave
        for (int fila = 0; fila < asteroides.getFila(); fila++) {
            for (int col = 0; col < asteroides.getColumna(); col++) {
                if (asteroides.getElement(fila, col) != null) {
                    asteroides.getElement(fila, col).draw(batch, this);
                    if (nave.checkCollision(asteroides.getElement(fila, col))) {
                        enemies.remove(asteroides.getElement(fila, col));
                        asteroides.delete(fila, col);
                    }   
                }
            }
        }
    }

    public void drawPowerUps(SpriteBatch batch) {
        for (PowerUp powerUp : powerUps) {
            batch.draw(powerUp.getTexture(), powerUp.getX(), powerUp.getY(), 32, 32); // Dibuja el power-up
        }
    }

    public void generarPowerUps() {
        PowerUp powerUp = new PowerUp(
            MathUtils.random(20, 700 - 25),
            MathUtils.random(300, 600),
            new Texture(Gdx.files.internal("powerup.png")),
            new MultiShootingStrategy(),
            2.0f
        );
        powerUps.add(powerUp);
    }

    public void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update();

            if (powerUp.isOutOfScreen()) {
                powerUps.remove(i); // Eliminar si sale de la pantalla
                i--; // Ajustar índice tras la eliminación
            }
        }
    }

    public void checkColisionPowerUp() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            
            if (nave.getArea().overlaps(powerUp.getArea())) {
                powerUp.apply(nave);
                powerUps.remove(i);
                powerUpDuration = 300;
                powerUpActive = true;
                break;
            }
        }
    }

    public void callGameOver() {
        if (score > SpaceNavigation.getInstance().getHighScore())
            SpaceNavigation.getInstance().setHighScore(score);
        Screen ss = new PantallaGameOver(SpaceNavigation.getInstance());
        ss.resize(1200, 800);
        SpaceNavigation.getInstance().setScreen(ss);
        dispose();
    }

    public void nextLevel() {
        Screen ss = new PantallaJuego(SpaceNavigation.getInstance(), ronda + 1, nave.getVidas(), score, 
                velXAsteroides + 3, cantAsteroides + 10);
        ss.resize(1200, 800);
        SpaceNavigation.getInstance().setScreen(ss);
        dispose();
    }

    @Override
    public void resume() {
        Preferences prefs = Gdx.app.getPreferences("GameState");
        score = prefs.getInteger("score", 0); // valor por defecto
        ronda = prefs.getInteger("ronda", 1); // valor por defecto
        nave.setVidas(prefs.getInteger("vidas", 3)); // valor por defecto
        cantAsteroides = prefs.getInteger("cantAsteroides", 10); // valor por defecto
        velXAsteroides = (int) prefs.getFloat("velXAsteroides", 5); // valor por defecto
        
        // Recrear asteroides
        int asteroidesCount = prefs.getInteger("asteroidesCount", 0);
        enemies.clear();
        for (int i = 0; i < asteroidesCount; i++) {
            float x = prefs.getFloat("asteroide_" + i + "_x", 0);
            float y = prefs.getFloat("asteroide_" + i + "_y", 0);
            float velX = prefs.getFloat("asteroide_" + i + "_velX", velXAsteroides);
            Ball2 asteroide = new Ball2(x, y, velX, new Texture(Gdx.files.internal("alienShip_1.png")));
            enemies.add(asteroide);
        }
        
        // Re-creamos la matriz de asteroides
        int filas = 3;
        int columnas = 10;
        asteroides = new MatrizBall2(filas, columnas);
        int index = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                if (index < enemies.size()) {
                    asteroides.setElement(filas, col, (Ball2) enemies.get(index++));
                } else {
                    asteroides.delete(fila, col);
                }
            }
        }
        
        gameMusic.play();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
    }
}


