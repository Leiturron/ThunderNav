package Class;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import AbstractClass.Enemy;
import Interface.Shootable;
import Screens.PantallaJuego;

public class Ball2 extends Enemy implements Shootable{
	private Texture txBalaEnemy;

    public Ball2(float x, float y, float xSpeed, Texture tx) {
        super(x, y, xSpeed, tx);
        spr.setSize(spr.getWidth(), spr.getHeight());
        spr.setPosition(x, y);
        txBalaEnemy = new Texture(Gdx.files.internal("Rocket2.png"));
    }
    
    @Override
    public void move() {
        // Mover verticalmente
        y -= movingRight ? speed : -speed; // Usar `speed` en lugar de `xSpeed`

        // Comprobar rebote en los bordes
        if (y + spr.getWidth() > 780 || y < 20) {
            movingRight = !movingRight; // Cambiar dirección
            // Bajar toda la formación al cambiar de dirección
            y -= 2; // Usar la altura del sprite para bajar
            // Asegurarse de que no baje demasiado
            if (y < 20) {
                y = 20; // Limitar la posición en y
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
	
	public void shoot(PantallaJuego juego) {
		if (y + spr.getWidth() >= 780) {
            Bullet bala = new Bullet(x + 20, y, 0, (int)speed + 3, txBalaEnemy);
            juego.agregarBalaEnemy(bala);
        }
	}
}



