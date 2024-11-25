package Builders;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import Class.Nave4;

public class Nave4Builder {
	private float x, y; // Posición
    private float speed; // Velocidad
    private Texture txNave;
    private Sound sonidoHerido;
    private Texture txBala;
    private Sound soundBala;
    
    public Nave4Builder setCoordenada(float x, float y) {
    	this.x = x;
    	this.y = y;
    	return this;
    }
    
    public Nave4Builder setSpeed(float speed) {
    	this.speed = speed;
    	return this;
    }
    
    public Nave4Builder setTexturaNave(Texture tx) {
    	this.txNave = tx;
    	return this;
    }
    
    public Nave4Builder setSonidoHerir(Sound sn) {
    	this.sonidoHerido = sn;
    	return this;
    }
    
    public Nave4Builder setTexturaBala(Texture tx) {
    	this.txBala = tx;
    	return this;
    }
    
    public Nave4Builder setSonidoBala(Sound sn) {
    	this.soundBala = sn;
    	return this;
    }
    
    public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSpeed() {
		return speed;
	}

	public Texture getTextureNave() {
		return txNave;
	}

	public Sound getSonidoHerido() {
		return sonidoHerido;
	}

	public Texture getTxBala() {
		return txBala;
	}

	public Sound getSoundBala() {
		return soundBala;
	}

	public Nave4 build() {
    	return new Nave4(this);
    }
}
