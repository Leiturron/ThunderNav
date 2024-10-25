package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball2 {
    private float x;
    private float y;
    private float xSpeed; // Velocidad horizontal
    private Sprite spr;
    private boolean movingRight = true; // Dirección del movimiento

    public Ball2(float x, float y, int size, float xSpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;
        this.y = y;

        // Validar que borde de esfera no quede fuera
        if (x - size < 0) this.x = size;
        if (x + size > Gdx.graphics.getWidth()) this.x = Gdx.graphics.getWidth() - size;

        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
    }

    public void update() {
        // Mover horizontalmente
        if (movingRight) {
            x += xSpeed;
            if (x + spr.getWidth() > Gdx.graphics.getWidth()) {
                movingRight = false; // Cambiar dirección
                y -= 10; // Baja la formación
            }
        } else {
            x -= xSpeed;
            if (x < 0) {
                movingRight = true; // Cambiar dirección
                y -= 10; // Baja la formación
            }
        }

        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public float getXSpeed() {
        return xSpeed;
    }
}
