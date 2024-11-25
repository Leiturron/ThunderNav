package Gestiones;

import AbstractClass.SpaceObject;

public interface GestionMoviment {
	public static void initMoviment(SpaceObject obj) {
		obj.move();
	}
}
