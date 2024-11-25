package io.github.FirstGame.testGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import Screens.PantallaMenu;

public class SpaceNavigation extends Game {

    // Instancia única para el patrón Singleton.
    private static SpaceNavigation instance;

    private String nombreJuego = "Thunder Ship";
    private SpriteBatch batch;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private int highScore;

    // Constructor privado para evitar instanciación directa.
    private SpaceNavigation() {}

    // Método público y estático para obtener la instancia única.
    public static SpaceNavigation getInstance() {
        if (instance == null) {
            instance = new SpaceNavigation();
        }
        return instance;
    }

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("vwFont.ttf"));
        FreeTypeFontParameter parametro = new FreeTypeFontParameter();
        parametro.size = 15;
        font = generator.generateFont(parametro);
        Screen ss = new PantallaMenu(SpaceNavigation.getInstance()); // Usa "this" ya que la clase sigue siendo un Game.
        this.setScreen(ss);
    }

    @Override
    public void render() {
        super.render(); // important!
    }

    @Override
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
