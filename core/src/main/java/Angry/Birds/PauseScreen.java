package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseScreen implements Screen {
    private int level;
    private Game game;
    private SpriteBatch batch;
    private Texture background;

    // Stage for handling UI elements
    private Stage stage;
    private GameScreen0 gameScreen; // Reference to the game screen
    private GameScreen gameScreen1; // Reference to the game screen

    private GameScreen2 gameScreen2; // Reference to the game screen



    // Buttons as ImageButtons
    private ImageButton restartButton;
    private ImageButton resumeButton;
    private ImageButton exitButton;
    private ImageButton settingsButton;

    public PauseScreen(final Game game,int level,GameScreen0 gameScreen,GameScreen gameScreen1,GameScreen2 gameScreen2) {
        this.level=level;
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture("green1.png"); // Background image
        this.stage = new Stage();
        this.gameScreen=gameScreen;
        this.gameScreen1=gameScreen1;
        this.gameScreen2=gameScreen2;
        Gdx.input.setInputProcessor(stage);

        // Load button textures
        Texture restartTexture = new Texture("restart.png");
        Texture resumeTexture = new Texture("resume.png");
        Texture exitTexture = new Texture("exit.png");
        Texture settingsTexture = new Texture("settings.png");

        // Create ImageButtons with the button textures
        resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));
        settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));

        // Set up button click listeners
        if (level==0) {
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("bey");
                    gameScreen.resumeGame(); // Resume the game
                    game.setScreen(gameScreen);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen(game)); // Navigate to NewPageScreen
                }
            });
        }else if(level==1){
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen1.resumeGame(); // Resume the game
                    game.setScreen(gameScreen1);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen1(game)); // Navigate to NewPageScreen
                }
            });
        }else{
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen2.resumeGame(); // Resume the game
                    game.setScreen(gameScreen2);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen2(game)); // Navigate to NewPageScreen
                }
            });
        }

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game)); // Navigate to HomeScreen
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Settings button does nothing for now
            }
        });

        // Add buttons to the stage
        stage.addActor(resumeButton);
        stage.addActor(restartButton);
        stage.addActor(exitButton);
        stage.addActor(settingsButton);

        // Set button positions to be centered and evenly spaced
        float buttonWidth = resumeButton.getWidth();
        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2; // Center X position

        // Adjust vertical positions to display resume at the top
        resumeButton.setPosition(centerX, 350);   // Resume button at the top
        restartButton.setPosition(centerX, 300);  // Restart button below
        exitButton.setPosition(centerX, 250);     // Exit button below restart
        settingsButton.setPosition(centerX, 200); // Settings button at the bottom
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing
        batch.begin();

        // Draw the background image (green.png)
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();

        // Draw the stage (buttons)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
    }
}
