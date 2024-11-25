package io.github.FirstGame.testGame;

import com.badlogic.gdx.graphics.Texture;

public interface ShootingStrategy {
    public void shoot(PantallaJuego juego, float x, float y, Texture txBala);
}

