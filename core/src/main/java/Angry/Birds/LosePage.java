package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class LosePage implements Screen {
    public static int keys=1;
    private int level;
    private Game game;
    private SpriteBatch batch;
    private Texture background;         // Background texture
    private Texture retryButtonTexture; // Retry button texture
    private Texture exitButtonTexture;  // Exit button texture
    private Texture keyTexture;         // Key texture
    private GameScreen0 gameScreen0;
    private GameScreen gameScreen1;
    private GameScreen2 gameScreen2;

    public LosePage(Game game,int level,GameScreen0 gameScreen0,GameScreen gameScreen1,GameScreen2 gameScreen2) {
        this.game = game;
        this.level=level;
        this.batch = new SpriteBatch();
        this.gameScreen0=gameScreen0;
        this.gameScreen1=gameScreen1;
        this.gameScreen2=gameScreen2;

        // Load textures
        background = new Texture("green.png");             // Background image
        retryButtonTexture = new Texture("new_retry.png");   // Retry button image
        exitButtonTexture = new Texture("exit.png");         // Exit button image
        keyTexture = new Texture("key.png");                  // Key texture image

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Draw the background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Define button sizes
        float buttonWidth = 128; // Width for buttons
        float buttonHeight = 64;  // Height for buttons

        // Calculate button positions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float retryButtonX = (screenWidth - buttonWidth) / 2; // Center horizontally
        float retryButtonY = (screenHeight / 2) - buttonHeight - 40; // Move further down from the center

        // Draw the retry button
        batch.draw(retryButtonTexture, retryButtonX, retryButtonY+130, buttonWidth, buttonHeight);

        // Draw the exit button below the retry button
        float exitButtonY = retryButtonY - buttonHeight - 30; // Space between buttons
        batch.draw(exitButtonTexture, retryButtonX, exitButtonY, buttonWidth, buttonHeight);

        if (keys == 1 && level==0) {
            float keyWidth = 64; // Width for the key texture
            float keyHeight = 64; // Height for the key texture
            float keyX = (screenWidth - keyWidth) / 2; // Center horizontally
            float keyY = (screenHeight - keyHeight) / 2; // Center vertically
            batch.draw(keyTexture, keyX+190, keyY+200, buttonWidth, buttonHeight); // Draw the key texture

            if (Gdx.input.isTouched()) {
                int x = Gdx.input.getX();
                int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate

                // Check if the key texture was clicked
                if (x >= keyX+190 && x <= keyX + keyWidth+190 && y >= keyY+200 && y <= 200+keyY + keyHeight) {
                    System.out.println("Key clicked");
                    goToLevel(); // Call method to switch to level
                }
            }

        }

        // Check for clicks on the retry and exit buttons
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate

            // Check if the retry button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= retryButtonY+130 && y <= retryButtonY + buttonHeight+130) {
                System.out.println("Retry button clicked");
                if (level == 0) {
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state.txt");

                    game.setScreen(new GameScreen0(game));
                } else if (level == 1) {
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state_1.txt");

                    game.setScreen(new GameScreen(game));// Replace with your GameScreen class
                } else if(level==2){
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state_2.txt");

                    game.setScreen(new GameScreen2(game));
                }
            }

            // Check if the exit button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= exitButtonY && y <= exitButtonY + buttonHeight) {
                System.out.println("Exit button clicked");
                if (level == 0) {
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state.txt");


                    game.setScreen(new NewPageScreen(game)); // Navigate to NewPageScreen
                }
                else if (level == 1) {
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state_1.txt");


                    game.setScreen(new NewPageScreen1(game)); // Navigate to NewPageScreen
                } else if(level==2){
                    flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state_2.txt");

                    game.setScreen(new NewPageScreen2(game)); // Navigate to NewPageScreen
                }
            }
        }

        batch.end();
    }

    public static void flushDataFromFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            // Opening in write mode without writing anything clears the file
            System.out.println("File contents cleared: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to clear the file: " + filePath);
        }
    }

    private void goToLevel() {
        // Logic to go to the next level and give the player a bird
        System.out.println("Going to the level with a bird!");

        // Implement logic for level transition and granting a bird
        if (level == 0) {
            game.setScreen(new GameScreen0(game));// Goes to level 0 screen
            keys--;
        } else if (level == 1) {
            game.setScreen(new GameScreen(game)); // Goes to level 1 screen
            keys--;
        } else if (level == 2) {
            game.setScreen(new GameScreen2(game)); // Goes to level 2 screen
            keys--;
        }

        // Grant a bird to the player (you'll have to implement this according to your game logic)
        // For example:
        // GameState.getInstance().addBird(new Bird("RedBird")); // A hypothetical method to add a bird
    }



    @Override
    public void resize(int width, int height) {}

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
        retryButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
