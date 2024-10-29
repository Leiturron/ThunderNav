package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Bullet {

	private int xSpeed;
	private int ySpeed;
	private boolean destroyed = false;
	private Sprite spr;
    
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    public void update() {
        spr.setPosition(spr.getX()+xSpeed, spr.getY()+ySpeed);
        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY()+spr.getHeight() > 780) {
        	destroyed = true;
        }
        
    }
    
    public void update2() {
        spr.setPosition(spr.getX() - xSpeed, spr.getY() - ySpeed);
        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 20 || spr.getY()+spr.getHeight() > 780) {
        	destroyed = true;
        }
        
    }
    
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public void draw2(SpriteBatch batch) {
    	spr.setRotation(180);
    	spr.draw(batch);
    }
    
    public boolean checkCollision(Ball2 b2) {
        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
        	// Se destruyen ambos
            this.destroyed = true;
            return true;

        }
        return false;
    }
    
    public boolean checkCollision(Nave4 nave) {
    	if(spr.getBoundingRectangle().overlaps(nave.getArea())){
            this.destroyed = true;
            return true;

        }
        return false;
    }
    
    public boolean isDestroyed() {return destroyed;}
    
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
}
