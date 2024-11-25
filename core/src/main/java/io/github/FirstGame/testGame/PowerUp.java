package io.github.FirstGame.testGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PowerUp {
    private float x, y;
    private float speed; // Velocidad de caída
    private Texture texture;
    private ShootingStrategy newStrategy; // Estrategia asociada al power-up

    public PowerUp(float x, float y, Texture texture, ShootingStrategy newStrategy, float speed) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.newStrategy = newStrategy;
        this.speed = speed;
    }

    // Mover el power-up hacia abajo
    public void update() {
        y -= speed; // Caída constante
    }

    // Aplicar el efecto del power-up a la nave
    public void apply(Nave4 nave) {
        nave.setShootingStrategy(newStrategy); // Cambiar la estrategia de disparo
    }

    public Rectangle getArea() {
        return new Rectangle(x, y, 32, 32);
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isOutOfScreen() {
        return y < 0; // Fuera de la pantalla
    }
}

