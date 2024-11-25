package io.github.FirstGame.testGame;


public interface GestionBullet {
	public static void initBullet(Shootable obj, PantallaJuego game) {
		obj.shoot(game);
	}
}
