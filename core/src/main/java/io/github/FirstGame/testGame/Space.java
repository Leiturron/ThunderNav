package io.github.FirstGame.testGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.function.Consumer;

public class Space {
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private Ball2[][] asteroides;
    private Nave4 nave; // Mantenemos la nave aquí
    private int score;

    public Space(int filas, int columnas, int velXAsteroides, int vidas) {
        this.nave = new Nave4(740 / 2 - 50, 30,
                new Texture(Gdx.files.internal("playerShip_1.png")),
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                new Texture(Gdx.files.internal("Rocket2.png")), 
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        this.nave.setVidas(vidas);
        
        crearAsteroides(filas, columnas, velXAsteroides);
    }


    private void crearAsteroides(int filas, int columnas, int velXAsteroides) {
        asteroides = new Ball2[filas][columnas];
        int espacioEntreBolas = 20;
        int size = 50;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                float x = col * (size + espacioEntreBolas);
                float y = Gdx.graphics.getHeight() - (fila * (size + espacioEntreBolas)) - size - 20;
                asteroides[fila][col] = new Ball2(x, y, velXAsteroides, new Texture(Gdx.files.internal("alienShip_1.png")));
                balls1.add(asteroides[fila][col]);
            }
        }
    }

    public void updateBullets(ArrayList<Bullet> balas, Sound explosionSound, Consumer<Integer> incrementScore) {
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            for (int fila = 0; fila < asteroides.length; fila++) {
                for (int col = 0; col < asteroides[fila].length; col++) {
                    if (asteroides[fila][col] != null && b.checkCollision(asteroides[fila][col])) {
                        explosionSound.play();
                        balls1.remove(asteroides[fila][col]);
                        incrementScore.accept(10);  // Aumentar el puntaje aquí
                        asteroides[fila][col] = null;
                    }
                }
            }
            if (b.isDestroyed()) {
                balas.remove(i);
                i--;
            }
        }
    }


    public void updateAsteroids() {
        for (int fila = 0; fila < asteroides.length; fila++) {
            for (int col = 0; col < asteroides[fila].length; col++) {
                if (asteroides[fila][col] != null) {
                    asteroides[fila][col].update();
                }
            }
        }
    }

    public void drawAsteroids(SpriteBatch batch, PantallaJuego pantallaJuego) {
        for (int fila = 0; fila < asteroides.length; fila++) {
            for (int col = 0; col < asteroides[fila].length; col++) {
                if (asteroides[fila][col] != null) {
                    asteroides[fila][col].draw(batch, pantallaJuego);
                }
            }
        }
    }

    public void checkCollisionWithNave() {
        for (Ball2 asteroide : balls1) {
            if (nave.checkCollision(asteroide)) {
                // Manejar colisión
                balls1.remove(asteroide);
                nave.setVidas(nave.getVidas() - 1);
            }
        }
    }

    public ArrayList<Ball2> getBalls1() {
        return balls1;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int value) {
        score += value;
    }

    public Nave4 getNave() {
        return nave; // Método para acceder a la nave
    }
}
