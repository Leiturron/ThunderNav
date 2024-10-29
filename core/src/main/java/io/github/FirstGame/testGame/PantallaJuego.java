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
    
    private Nave4 nave;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    // Matriz de enemigos
    private Ball2[][] asteroides;

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
        
        nave = new Nave4(740/2 - 50, 30,
                new Texture(Gdx.files.internal("playerShip_1.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        crearAsteroides();
    }  

    private void crearAsteroides() {
        
        // Definir tamaño de la matriz
        int columnas = 10; // Número de columnas
        int filas = 3; // Número de filas
        asteroides = new Ball2[filas][columnas];
        
        int espacioEntreBolas = 20; // Espacio entre las bolas
        int size = 50; // Tamaño de las bolas
        int velocidadConstante = velXAsteroides; // Velocidad constante para todas las bolas
 
        // Crear enemigos en matriz
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                float x = col * (size + espacioEntreBolas); // Espaciado horizontal
                float y = Gdx.graphics.getHeight() - (fila * (size + espacioEntreBolas)) - size - 20; // Espaciado vertical
                asteroides[fila][col] = new Ball2(x, y, velocidadConstante, 
                                                    new Texture(Gdx.files.internal("alienShip_1.png")));
                enemies.add(asteroides[fila][col]); // También agregar a balls1 para la lógica de colisión
            }
        }
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
        
        if (!nave.estaHerido()) {
            // Colisiones entre balas y enemigos y su destrucción  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int fila = 0; fila < asteroides.length; fila++) {
                    for (int col = 0; col < asteroides[fila].length; col++) {
                        if (asteroides[fila][col] != null && b.checkCollision(asteroides[fila][col])) {          
                            explosionSound.play();
                            enemies.remove(asteroides[fila][col]);
                            asteroides[fila][col] = null; // Marcar como destruido
                            score += 10;
                        } 
                    }
                }
                
                if (b.isDestroyed()) {
                    balas.remove(i);
                    i--; // Para no saltarse uno tras eliminar del ArrayList
                }
            }
            
            // Actualizar movimiento de enemigos dentro del área
            for (int fila = 0; fila < asteroides.length; fila++) {
                for (int col = 0; col < asteroides[fila].length; col++) {
                    if (asteroides[fila][col] != null) {
                        asteroides[fila][col].update();
                    }
                }
            }
        }
        
        // Dibujar balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        
        nave.draw(batch, this);
        
        // Dibujar enemigos y manejar colisión con nave
        for (int fila = 0; fila < asteroides.length; fila++) {
            for (int col = 0; col < asteroides[fila].length; col++) {
                if (asteroides[fila][col] != null) {
                    asteroides[fila][col].draw(batch, this); // Cambié aquí para usar el nuevo método draw
                    // Perdió vida o game over
                    if (nave.checkCollision(asteroides[fila][col])) {
                        // Enemigo se destruye con el choque             
                        enemies.remove(asteroides[fila][col]);
                        asteroides[fila][col] = null; // Marcar como destruido
                    }   
                }
            }
        }
        
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
        
        batch.end();
        
        // Nivel completado
        if (enemies.size() == 0) {
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score, 
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
        asteroides = new Ball2[filas][columnas];
        int index = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                if (index < enemies.size()) {
                    asteroides[fila][col] = (Ball2) enemies.get(index++);
                } else {
                    asteroides[fila][col] = null; // No hay más asteroides
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
