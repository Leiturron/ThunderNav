package io.github.FirstGame.testGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class SpaceObject{
    protected float x, y; // Posición
    protected float speed; // Velocidad

    public SpaceObject(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void action(SpriteBatch batch, PantallaJuego juego) {
    	  draw(batch, juego); 
          move(); 
    }

    public abstract void draw(SpriteBatch batch, PantallaJuego juego); // Método abstracto para dibujar el objeto
    public abstract void move(); // Método abstracto para mover el objeto
}

