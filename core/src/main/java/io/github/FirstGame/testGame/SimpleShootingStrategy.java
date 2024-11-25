package io.github.FirstGame.testGame;

import com.badlogic.gdx.graphics.Texture;

public class SimpleShootingStrategy implements ShootingStrategy {
    @Override
    public void shoot(PantallaJuego juego, float x, float y, Texture txBala) {
        Bullet bala = new Bullet(x, y, 0, 3, txBala);
        juego.agregarBala(bala);
    }
}

