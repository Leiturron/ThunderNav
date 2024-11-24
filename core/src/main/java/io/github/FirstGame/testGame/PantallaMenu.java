package io.github.FirstGame.testGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

	private Stage stage;
	private SpriteBatch batch;
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private ImageButton playButton;
    private Texture buttonTex;
    private ImageButton quitButton;
    private Texture quitTex;
    private Texture fondo;

    public PantallaMenu(SpaceNavigation game) {
        this.game = SpaceNavigation.getInstance();
                
        stage = new Stage();
        this.fondo = new Texture(Gdx.files.internal("menu.png"));
        this.batch = new SpriteBatch();
        
        this.buttonTex = new Texture(Gdx.files.internal("play.png"));
        this.playButton = new ImageButton(new TextureRegionDrawable(buttonTex));
        
        this.quitTex = new Texture(Gdx.files.internal("quit.png"));
        this.quitButton = new ImageButton(new TextureRegionDrawable(quitTex));
        
        this.playButton.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				int ronda = 1;
	            int vidas = 3;
	            int score = 0;
	            int velXAsteroides = 1;
	            int cantAsteroides = 10;

	            Screen ss = new PantallaJuego(game, ronda, vidas, score, velXAsteroides, cantAsteroides);
	            ss.resize(1200, 800);
	            game.setScreen(ss);
	            dispose();
	            stage.dispose();
	            return true;
			}
        	
        });
        
        this.quitButton.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				System.exit(0);
				return true;
			}
        	
        });
        
        playButton.setPosition(900, 450);
        stage.addActor(playButton);
        
        quitButton.setPosition(900, 300);
        stage.addActor(quitButton);
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        
        camera.update();
        
        batch.begin();
        batch.draw(fondo, 0, 0, 1200, 800);
        batch.end();
        stage.act();
        stage.draw();
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
