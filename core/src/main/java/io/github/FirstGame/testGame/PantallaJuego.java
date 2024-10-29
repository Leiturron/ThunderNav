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

    private Texture gameFondo;
    
    private ArrayList<Bullet> balas = new ArrayList<>();
    private Space space;

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.cantAsteroides = cantAsteroides;

        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        
        gameFondo = new Texture(Gdx.files.internal("GameFondo.png"));
        
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Creación de Space, la cual se encargará de crear la nave
        space = new Space(3, 10, velXAsteroides, vidas);
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + space.getNave().getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, "" + this.score, 910, 800 - 76);
        game.getFont().draw(batch, "" + game.getHighScore(), 910, 800 - 41);
        game.getFont().draw(batch, "" + space.getNave().getVidas(), 860, 800 - 280);
        game.getFont().draw(batch, "" + ronda, 860, 800 - 315);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(gameFondo, 0, 0, 1200, 800);
        dibujaEncabezado();
        
        if (!space.getNave().estaHerido()) {
            // Actualiza las balas y verifica colisiones, pasando la lógica de puntaje
            space.updateBullets(balas, explosionSound, scoreIncrement -> {
                score += scoreIncrement; // Aumentar el puntaje local
            });
            // Actualiza los asteroides
            space.updateAsteroids();
            // Verifica colisiones con la nave
            space.checkCollisionWithNave();
        }
        
        // Dibuja las balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        
        space.getNave().draw(batch, this);
        
        // Dibuja los asteroides
        space.drawAsteroids(batch, this);
        
        if (space.getNave().estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
        
        batch.end();
        
        // Nivel completado
        if (space.getBalls1().isEmpty()) {
            Screen ss = new PantallaJuego(game, ronda + 1, space.getNave().getVidas(), score, 
                    velXAsteroides + 3, cantAsteroides + 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    
    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
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
        prefs.putInteger("vidas", space.getNave().getVidas());
        prefs.putInteger("cantAsteroides", cantAsteroides);
        prefs.putFloat("velXAsteroides", velXAsteroides);
        prefs.putInteger("asteroidesCount", space.getBalls1().size());
        
        // Guardar el estado de cada asteroide
        for (int i = 0; i < space.getBalls1().size(); i++) {
            Ball2 asteroide = space.getBalls1().get(i);
            prefs.putFloat("asteroide_" + i + "_x", asteroide.getX());
            prefs.putFloat("asteroide_" + i + "_y", asteroide.getY());
            prefs.putFloat("asteroide_" + i + "_velX", asteroide.getXSpeed());
        }
        prefs.flush(); // Guarda los cambios
    }

    @Override
    public void resume() {
        Preferences prefs = Gdx.app.getPreferences("GameState");
        score = prefs.getInteger("score", 0); // valor por defecto
        ronda = prefs.getInteger("ronda", 1); // valor por defecto
        space.getNave().setVidas(prefs.getInteger("vidas", 3)); // valor por defecto
        cantAsteroides = prefs.getInteger("cantAsteroides", 10); // valor por defecto
        velXAsteroides = (int) prefs.getFloat("velXAsteroides", 5); // valor por defecto
        
        // Recrear asteroides
        int asteroidesCount = prefs.getInteger("asteroidesCount", 0);
        for (int i = 0; i < asteroidesCount; i++) {
            float x = prefs.getFloat("asteroide_" + i + "_x", 0);
            float y = prefs.getFloat("asteroide_" + i + "_y", 0);
            float velX = prefs.getFloat("asteroide_" + i + "_velX", velXAsteroides);
            Ball2 asteroide = new Ball2(x, y, velX, new Texture(Gdx.files.internal("alienShip_1.png")));
            space.getBalls1().add(asteroide);
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

