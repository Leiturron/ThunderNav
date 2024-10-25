package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaGameOver implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;

    public PantallaGameOver(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Game Over !!!", 120, 400, 400, 1, true);
        game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar ...", 100, 300);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            // Ajustar los parámetros de la nueva pantalla de juego según lo que necesites
            int ronda = 1;
            int vidas = 3;
            int score = 0;
            int velXAsteroides = 1;
            int cantAsteroides = 10;

            Screen ss = new PantallaJuego(game, ronda, vidas, score, velXAsteroides, cantAsteroides);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    @Override
    public void show() {
        // Aquí podrías inicializar cualquier recurso necesario al mostrar esta pantalla
    }

    @Override
    public void resize(int width, int height) {
        // Manejar el redimensionamiento si es necesario
    }

    @Override
    public void pause() {
        // Manejar la pausa si es necesario
    }

    @Override
    public void resume() {
        // Manejar la reanudación si es necesario
    }

    @Override
    public void hide() {
        // Manejar lo que sucede al ocultar esta pantalla
    }

    @Override
    public void dispose() {
        // Liberar recursos si es necesario
    }
}
