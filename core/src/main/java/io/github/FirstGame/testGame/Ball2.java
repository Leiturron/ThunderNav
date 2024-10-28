package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball2 extends Enemy {

    public Ball2(float x, float y, float xSpeed, Texture tx) {
        super(x, y, xSpeed, tx);
        spr.setSize(spr.getWidth(), spr.getHeight());
        spr.setPosition(x, y);
    }

    @Override
    public void update() {
        // Mover horizontalmente
        x += movingRight ? speed : -speed; // Usar `speed` en lugar de `xSpeed`

        // Comprobar rebote en los bordes
        if (x + spr.getWidth() > Gdx.graphics.getWidth() || x < 0) {
            movingRight = !movingRight; // Cambiar dirección
            // Bajar toda la formación al cambiar de dirección
            y -= spr.getHeight(); // Usar la altura del sprite para bajar
            // Asegurarse de que no baje demasiado
            if (y < 0) {
                y = 0; // Limitar la posición en y
            }
        }

        spr.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        super.draw(batch, juego); // Usar el método `draw` de la clase padre
    }

	@Override
	public float getXSpeed() {
		return speed;
	}
}



