package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 extends SpaceObject{

    private boolean destruida = false;
    private int vidas = 3;
    private float speed = 5; // Velocidad de movimiento
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	super(x, y);
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        if (!herido) {
            // Manejo del movimiento con teclado
            move();

            // Asegúrate de que la nave se mantenga dentro de los bordes de la pantalla
            if (spr.getX() < 20) {
                spr.setX(20); // Límite izquierdo
            } else if (spr.getX() + spr.getWidth() > 700) {
                spr.setX(700 - spr.getWidth()); // Límite derecho
            }

            spr.draw(batch);
        } else {
            // Efecto de herido
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // Lógica de disparo
        shoot(juego);
    }

    // Método para manejar el movimiento con teclado
    public void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            spr.setX(spr.getX() - speed); // Mueve a la izquierda
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            spr.setX(spr.getX() + speed); // Mueve a la derecha
        }
    }

    // Método para disparar
    public void shoot(PantallaJuego juego) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Lógica de colisión
            // (Se mantiene tu lógica de colisión aquí)
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0)
                destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }
}

