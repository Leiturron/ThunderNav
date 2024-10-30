package io.github.FirstGame.testGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;




public class SpaceNavigation extends Game {
	private String nombreJuego = "Thunder Ship";
	private SpriteBatch batch;
	private BitmapFont font;
	private FreeTypeFontGenerator generator;
	private int highScore;	

	public void create() {
		highScore = 0;
		batch = new SpriteBatch();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("vwFont.ttf"));
		FreeTypeFontParameter parametro = new FreeTypeFontParameter();
		parametro.size = 15;
		font = generator.generateFont(parametro);
		Screen ss = new PantallaMenu(this);
		this.setScreen(ss);
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	
	

}