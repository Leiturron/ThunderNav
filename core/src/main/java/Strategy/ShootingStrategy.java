package Strategy;

import com.badlogic.gdx.graphics.Texture;

import Screens.PantallaJuego;

public interface ShootingStrategy {
    public void shoot(PantallaJuego juego, float x, float y, Texture txBala);
}

