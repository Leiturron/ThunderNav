package AbstractClass;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import Screens.PantallaJuego;

public abstract class Enemy extends SpaceObject{
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

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        spr.draw(batch);
    }

    @Override
    public abstract void move();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
	public abstract float getXSpeed();
}


