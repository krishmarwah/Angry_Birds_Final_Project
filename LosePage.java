package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class LosePage implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture background;         // Background texture
    private Texture levelFailedTexture; // "Level Failed" texture
    private Texture retryButtonTexture; // Retry button texture
    private Texture exitButtonTexture;  // Exit button texture

    public LosePage(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();

        // Load textures
        background = new Texture("green.png");             // Background image
        levelFailedTexture = new Texture("level_failed.png"); // "Level Failed" image
        retryButtonTexture = new Texture("new_retry.png");   // Retry button image
        exitButtonTexture = new Texture("exit.png");         // Exit button image
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

        // Draw "Level Failed" centered at the top
        float screenWidth = Gdx.graphics.getWidth();
        float levelFailedX = (screenWidth - levelFailedTexture.getWidth()) / 2;
        float levelFailedY = Gdx.graphics.getHeight() - levelFailedTexture.getHeight() - 50; // Some margin from top
        batch.draw(levelFailedTexture, levelFailedX - 50, levelFailedY, levelFailedTexture.getWidth() * 1.5f, levelFailedTexture.getHeight() * 1.5f);

        // Define button sizes
        float buttonWidth = 128; // New width for buttons
        float buttonHeight = 64;  // New height for buttons

        // Calculate button positions
        float retryButtonX = (screenWidth - buttonWidth) / 2; // Center horizontally
        float retryButtonY = levelFailedY - buttonHeight - 20; // Space below "Level Failed"

        // Draw the retry button
        batch.draw(retryButtonTexture, retryButtonX, retryButtonY, buttonWidth, buttonHeight);

        // Draw the exit button below the retry button
        float exitButtonY = retryButtonY - buttonHeight - 20; // Space between buttons
        batch.draw(exitButtonTexture, retryButtonX, exitButtonY, buttonWidth, buttonHeight);

        // Check for clicks on the retry and exit buttons
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate

            // Check if the retry button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= retryButtonY && y <= retryButtonY + buttonHeight) {
                System.out.println("Retry button clicked");
                game.setScreen(new GameScreen(game)); // Replace with your GameScreen class
            }

            // Check if the exit button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= exitButtonY && y <= exitButtonY + buttonHeight) {
                System.out.println("Exit button clicked");
                game.setScreen(new NewPageScreen(game)); // Navigate to NewPageScreen
            }
        }

        batch.end();
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
        levelFailedTexture.dispose();
        retryButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
