package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class SuccessPage implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture background;         // Background texture
    private Texture catapult;           // Catapult texture
    private Texture nextButtonTexture;  // Next button texture
    private Texture retryButtonTexture; // Retry button texture

    public SuccessPage(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();

        // Load all required textures
        background = new Texture("bg.png");
        catapult = new Texture("catapult.png");
        nextButtonTexture = new Texture("next.png");     // Load next button texture
        retryButtonTexture = new Texture("retry.png");   // Load retry button texture
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_BLEND);

        batch.begin();

        // Draw the background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the catapult
        float catapultX = Gdx.graphics.getWidth() / 4.8f;
        float catapultY = 125;
        float catapultWidth = 50;
        float catapultHeight = 50;
        batch.draw(catapult, catapultX, catapultY, catapultWidth, catapultHeight);

        // Draw the next button at the bottom right
        float nextButtonX = Gdx.graphics.getWidth() - 74; // Adjust for button width
        float nextButtonY = 10;                           // Bottom margin
        batch.draw(nextButtonTexture, nextButtonX, nextButtonY, 64, 64);

        // Draw the retry button at the bottom left
        float retryButtonX = 10;                          // Left margin
        float retryButtonY = 10;                          // Bottom margin
        batch.draw(retryButtonTexture, retryButtonX, retryButtonY, 64, 64);

        // Check for clicks on the next and retry buttons
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the click is inside the next button bounds
            if (x >= nextButtonX && x <= nextButtonX + 64 && y >= nextButtonY && y <= nextButtonY + 64) {
                System.out.println("Next button clicked");
                game.setScreen(new WinPage(game));  // Transition to WinPage
            }

           // Check if the click is inside the retry button bounds
            if (x >= retryButtonX && x <= retryButtonX + 64 && y >= retryButtonY && y <= retryButtonY + 64) {
                System.out.println("Retry button clicked");
                game.setScreen(new LosePage(game));  // Transition to LosePage
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
        catapult.dispose();
        nextButtonTexture.dispose();   // Dispose of next button texture
        retryButtonTexture.dispose();  // Dispose of retry button texture
    }
}
