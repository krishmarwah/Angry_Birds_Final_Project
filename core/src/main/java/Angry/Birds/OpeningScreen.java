package Angry.Birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class OpeningScreen implements Screen {
    private Texture background; // Texture for the loading screen
    private Texture nextTexture; // Texture for the next button
    private Main game; // Reference to the main Game instance
    private SpriteBatch batch; // Use a single SpriteBatch for rendering

    private float buttonWidth = 128; // Width of the next button
    private float buttonHeight = 64; // Height of the next button
    private float buttonX; // X-position for the next button
    private float buttonY; // Y-position for the next button

    public OpeningScreen(Main game) {
        this.game = game;
        background = new Texture("loading_screen.png"); // Load the loading screen texture
        nextTexture = new Texture("next.png"); // Load the next button texture
        batch = new SpriteBatch(); // Initialize the SpriteBatch
    }

    @Override
    public void show() {
        // Calculate the position for the next button to be at the absolute bottom right
        buttonX = Gdx.graphics.getWidth() - buttonWidth; // Align with the right edge
        buttonY = 0; // Align with the bottom edge
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // Clear the screen

        batch.begin(); // Start rendering with the SpriteBatch

        // Draw the background texture
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the next button at the bottom right
        batch.draw(nextTexture, buttonX, buttonY, buttonWidth, buttonHeight);

        // Check for input to switch screens
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y-axis

            // Check if the touch is within the bounds of the next button
            if (touchX >= buttonX && touchX <= buttonX + buttonWidth &&
                touchY >= buttonY && touchY <= buttonY + buttonHeight) {
                this.dispose(); // Dispose of resources when transitioning to the next screen
                game.setScreen(new StartScreen(game)); // Switch to the StartScreen
            }
        }

        batch.end(); // End rendering with the SpriteBatch
    }

    @Override
    public void resize(int width, int height) {
        // Logic to resize can be added if necessary
    }

    @Override
    public void pause() {
        // Logic to handle pausing the game can be added here if necessary
    }

    @Override
    public void resume() {
        // Logic to resume the game can be added here if necessary
    }

    @Override
    public void hide() {
        // Logic to hide the screen can be added here if necessary
    }

    @Override
    public void dispose() {
        background.dispose(); // Dispose of the loading screen texture
        nextTexture.dispose(); // Dispose of the next button texture
        batch.dispose(); // Dispose of the SpriteBatch
    }
}
