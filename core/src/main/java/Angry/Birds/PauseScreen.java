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
import com.badlogic.gdx.Preferences;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class PauseScreen implements Screen {
    private int level;
    private Game game;
    private SpriteBatch batch;
    private Texture background;

    private Stage stage;
    private GameScreen0 gameScreen;
    private GameScreen gameScreen1;
    private GameScreen2 gameScreen2;

    private ImageButton restartButton;
    private ImageButton resumeButton;
    private ImageButton exitButton;
    private ImageButton settingsButton;

    // Preferences for saving the game state
    private Preferences prefs;

    public PauseScreen(final Game game, int level, GameScreen0 gameScreen, GameScreen gameScreen1, GameScreen2 gameScreen2) {
        this.level = level;
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture("green1.png");
        this.stage = new Stage();
        this.gameScreen = gameScreen;
        this.gameScreen1 = gameScreen1;
        this.gameScreen2 = gameScreen2;

        Gdx.input.setInputProcessor(stage);

        // Initialize Preferences for saving the game state
        prefs = Gdx.app.getPreferences("MyGame");

        // Load button textures
        Texture restartTexture = new Texture("restart.png");
        Texture resumeTexture = new Texture("resume.png");
        Texture exitTexture = new Texture("exit.png");
        Texture settingsTexture = new Texture("save.png");

        // Create ImageButtons with the button textures
        resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));
        settingsButton = new ImageButton(new TextureRegionDrawable(settingsTexture));

        // Set up button click listeners
        if (level == 0) {
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen.resumeGame();
                    game.setScreen(gameScreen);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen(game));
                }
            });
        } else if (level == 1) {
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen1.resumeGame();
                    game.setScreen(gameScreen1);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen1(game));
                }
            });
        } else {
            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameScreen2.resumeGame();
                    game.setScreen(gameScreen2);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new NewPageScreen2(game));
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
                saveGameState();
            }
        });

        // Add buttons to the stage
        stage.addActor(resumeButton);
        stage.addActor(restartButton);
        stage.addActor(exitButton);
        stage.addActor(settingsButton);

        // Set button positions
        float buttonWidth = resumeButton.getWidth();
        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2;

        resumeButton.setPosition(centerX, 350);
        restartButton.setPosition(centerX, 300);
        exitButton.setPosition(centerX, 250);
        settingsButton.setPosition(centerX, 200);
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
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw the stage
        stage.act(delta);
        stage.draw();
    }

    private void saveGameState() {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\Desktop\\save_game_state.txt");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Save the positions of objects in GameScreen0 (example for level 0)
            if (level == 0 && gameScreen != null) {
                objectOut.writeObject(gameScreen.getWoodBlockPositions());// Save wood block positions
                objectOut.writeObject(gameScreen.getpigPositions());
                objectOut.writeObject(gameScreen.getBirdQueue());
            }

            Gdx.app.log("Save", "Game state saved successfully!");
        } catch (Exception ex) {
            Gdx.app.log("Save", "Error saving game state: " + ex.getMessage());
        }

        try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\Desktop\\save_game_state_1.txt");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Save the positions of objects in GameScreen0 (example for level 0)
            if (level == 1 && gameScreen1 != null) {
                objectOut.writeObject(gameScreen1.getWoodBlockPositions());// Save wood block positions
                objectOut.writeObject(gameScreen1.glassPositions());
                objectOut.writeObject(gameScreen1.getpigPositions());
                objectOut.writeObject(gameScreen1.getBirdQueue());
            }

            Gdx.app.log("Save", "Game state saved successfully!");
        } catch (Exception ex) {
            Gdx.app.log("Save", "Error saving game state: " + ex.getMessage());
        }

        try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\Desktop\\save_game_state_2.txt");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Save the positions of objects in GameScreen0 (example for level 0)
            if (level == 2 && gameScreen2 != null) {
                objectOut.writeObject(gameScreen2.getWoodBlockPositions());// Save wood block positions
                objectOut.writeObject(gameScreen2.glassPositions());
                objectOut.writeObject(gameScreen2.getstonepositions());
                objectOut.writeObject(gameScreen2.getpigPositions());
                objectOut.writeObject(gameScreen2.getBirdQueue());
            }

            Gdx.app.log("Save", "Game state saved successfully!");
        } catch (Exception ex) {
            Gdx.app.log("Save", "Error saving game state: " + ex.getMessage());
        }
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
