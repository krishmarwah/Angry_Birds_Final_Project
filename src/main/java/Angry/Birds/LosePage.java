package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class LosePage implements Screen {
    private int level;
    private Game game;
    private SpriteBatch batch;
    private Texture background;         // Background texture
    private Texture retryButtonTexture; // Retry button texture
    private Texture exitButtonTexture;  // Exit button texture

    public LosePage(Game game,int level) {
        this.game = game;
        this.level=level;
        this.batch = new SpriteBatch();

        // Load textures
        background = new Texture("green.png");             // Background image
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

        // Check for clicks on the retry and exit buttons
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate

            // Check if the retry button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= retryButtonY+130 && y <= retryButtonY + buttonHeight+130) {
                System.out.println("Retry button clicked");
                if (level == 0) {
                    game.setScreen(new GameScreen0(game));
                } else if (level == 1) {
                    game.setScreen(new GameScreen(game));// Replace with your GameScreen class
                } else if(level==2){
                    game.setScreen(new GameScreen2(game));
                }
            }

            // Check if the exit button was clicked
            if (x >= retryButtonX && x <= retryButtonX + buttonWidth && y >= exitButtonY && y <= exitButtonY + buttonHeight) {
                System.out.println("Exit button clicked");
                if (level == 0) {
                    game.setScreen(new NewPageScreen(game)); // Navigate to NewPageScreen
                } else if (level == 1) {
                    game.setScreen(new NewPageScreen1(game)); // Navigate to NewPageScreen
                } else if(level==2){
                    game.setScreen(new NewPageScreen2(game)); // Navigate to NewPageScreen
                }
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
        retryButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
