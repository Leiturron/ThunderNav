package Gestiones;

import Interface.Shootable;
import Screens.PantallaJuego;

public interface GestionBullet {
	public static void initBullet(Shootable obj, PantallaJuego game) {
		obj.shoot(game);
	}
}
