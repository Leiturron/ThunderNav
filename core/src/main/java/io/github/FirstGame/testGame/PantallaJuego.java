package io.github.FirstGame.testGame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
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
    
    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> balls2 = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

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
        
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        
        nave = new Nave4(Gdx.graphics.getWidth()/2 - 50, 30,
                new Texture(Gdx.files.internal("playerShip_1.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        Random r = new Random();
     // Crear asteroides en formación
     // Crear asteroides en formación
        int columnas = 10; // Número de columnas
        int filas = 3; // Número de filas
        int espacioEntreBolas = 50; // Espacio entre las bolas
        int size = 20; // Tamaño de las bolas
        float velocidadConstante = velXAsteroides + r.nextInt(4); // Velocidad constante para todas las bolas

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                float x = col * (size + espacioEntreBolas); // Espaciado horizontal
                float y = Gdx.graphics.getHeight() - (fila * (size + espacioEntreBolas)) - (size * 3); // Espaciado vertical
                Ball2 bb = new Ball2(x, y, size, velocidadConstante, 
                                      new Texture(Gdx.files.internal("alienShip_1.png")));
                balls1.add(bb);
                balls2.add(bb);
            }
        }
    }    

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();
        
        if (!nave.estaHerido()) {
            // Colisiones entre balas y asteroides y su destrucción  
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {    
                    if (b.checkCollision(balls1.get(j))) {          
                        explosionSound.play();
                        balls1.remove(j);
                        j--;
                        score += 10;
                    } 
                }
                
                if (b.isDestroyed()) {
                    balas.remove(i);
                    i--; // Para no saltarse uno tras eliminar del ArrayList
                }
            }
            
            // Actualizar movimiento de asteroides dentro del área
            for (Ball2 ball : balls1) {
                ball.update();
            }
        }
        
        // Dibujar balas
        for (Bullet b : balas) {       
            b.draw(batch);
        }
        
        nave.draw(batch, this);
        
        // Dibujar asteroides y manejar colisión con nave
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            b.draw(batch);
            // Perdió vida o game over
            if (nave.checkCollision(b)) {
                // Asteroide se destruye con el choque             
                balls1.remove(i);
                i--;
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
        if (balls1.size() == 0) {
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
    }
}
