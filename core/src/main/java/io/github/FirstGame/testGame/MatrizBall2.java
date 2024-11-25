package io.github.FirstGame.testGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MatrizBall2 {
	private Ball2[][] miMatriz;
	private int fila;
	private int columna;
	private int espacio;
	private int sizeElement;
	
	public MatrizBall2(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
		miMatriz = new Ball2[fila][columna];
	}
	
	public MatrizBall2(int fila, int columna, int espacio, int sizeElement) {
		this.fila = fila;
		this.columna = columna;
		this.espacio = espacio;
		this.sizeElement = sizeElement;
		miMatriz = new Ball2[fila][columna];
	}
	
	public void setDimension(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
		miMatriz = new Ball2[fila][columna];
	}

	public int getSizeElement() {
		return sizeElement;
	}

	public void setSizeElement(int sizeElement) {
		this.sizeElement = sizeElement;
	}

	public int getSetEspacio() {
		return espacio;
	}

	public void setEspacio(int espacio) {
		this.espacio = espacio;
	}

	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}

	public void CrearEnemigos(ArrayList<Enemy> enemies, int vel) {
		// Crear enemigos en matriz
        for (int row = 0; row < fila; row++) {
            for (int col = 0; col < columna; col++) {
                float x = col * (sizeElement + espacio); // Espaciado horizontal
                float y = Gdx.graphics.getHeight() - (row * (sizeElement + espacio)) - sizeElement - 20; // Espaciado vertical
                miMatriz[row][col] = new Ball2(x, y, vel, new Texture(Gdx.files.internal("alienShip_1.png")));
                enemies.add(miMatriz[row][col]); // También agregar a balls1 para la lógica de colisión
            }
        }
	}
	
	public Ball2 getElement(int fila, int columna) {
		return miMatriz[fila][columna];
	}
	
	public void delete(int fila, int columna) {
		miMatriz[fila][columna] = null;
	}
	
	public void setElement(int fila, int columna, Ball2 enemy) {
		miMatriz[fila][columna] = enemy;
	}
	
	public void initEnemy(PantallaJuego game) {
    	// Actualizar movimiento de enemigos dentro del área
        for (int row = 0; row < fila; row++) {
            for (int col = 0; col < columna; col++) {
                if (miMatriz[row][col] != null) {
                    GestionMoviment.initMoviment(miMatriz[row][col]);
                    GestionBullet.initBullet(miMatriz[row][col], game);
                }
            }
        }
    }
	
}
