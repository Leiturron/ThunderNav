package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy extends SpaceObject implements Movible{
    protected Sprite spr;
    protected boolean movingRight = true; // Dirección del movimiento

    public Enemy(float x, float y, float xSpeed, Texture tx) {
        super(x, y); // Llama al constructor de SpaceObject
        this.speed = xSpeed; // Asignar velocidad desde el constructor
        spr = new Sprite(tx);

        // Validar que el borde del enemigo no quede fuera
        if (x < 20) this.x = 20;
        if (x + spr.getWidth() > 680) this.x = 680 - spr.getWidth();

        spr.setPosition(x, y);
    }

    public abstract void update(); // Método abstracto que debe ser implementado en las subclases

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        spr.draw(batch);
    }

    @Override
    public void move() {
        // Método vacío ya que la lógica de movimiento puede variar entre enemigos
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
	public abstract float getXSpeed();
}


