package io.github.FirstGame.testGame;

import com.badlogic.gdx.graphics.Texture;

public class MultiShootingStrategy implements ShootingStrategy {
    @Override
    public void shoot(PantallaJuego juego, float x, float y, Texture txBala) {
        juego.agregarBala(new Bullet(x - 10, y, -1, 3, txBala));
        juego.agregarBala(new Bullet(x, y, 0, 3, txBala));
        juego.agregarBala(new Bullet(x + 10, y, 1, 3, txBala));
    }
}

