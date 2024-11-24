package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;

public class StartScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont font;

    private Texture playButtonTexture; // Texture for the play button
    private Texture rulesButtonTexture; // Texture for the rules button
    private Texture exitButtonTexture; // Texture for the exit button

    private float buttonWidth = 120; // Smaller button width
    private float buttonHeight = 30; // Smaller button height
    private float startX, startY;

    private float inputCooldown = 0.2f;  // Cooldown time to prevent immediate actions on the next screen
    private float cooldownTimer = 0f;

    // Pass a reference to the Game object to handle screen switching
    public StartScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("check.png"); // Change the background texture
        font = new BitmapFont();
        font.getData().setScale(2);

        // Load button textures
        playButtonTexture = new Texture("new_play.png");
        rulesButtonTexture = new Texture("rules.png");
        exitButtonTexture = new Texture("new_exit.png");

        updateButtonPositions();
    }

    @Override
    public void render(float delta) {
        // Update the cooldown timer
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        // Calculate button positions based on the current screen size
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float imgWidth = background.getWidth();
        float imgHeight = background.getHeight();
        float scale = Math.max(screenWidth / imgWidth, screenHeight / imgHeight);
        float scaledWidth = imgWidth * scale;
        float scaledHeight = imgHeight * scale;
        float x = (screenWidth - scaledWidth) / 2;
        float y = (screenHeight - scaledHeight) / 2;

        batch.draw(background, x, y, scaledWidth, scaledHeight);

        // Draw buttons with textures instead of text
        float playButtonY = startY + 2 * (buttonHeight + 10) - 25;  // Calculate y position for Play button
        batch.draw(playButtonTexture, startX + 8, playButtonY, buttonWidth, buttonHeight); // Play button

        float rulesButtonY = startY + (buttonHeight + 10) - 25; // Calculate y position for Rules button
        batch.draw(rulesButtonTexture, startX + 8, rulesButtonY, buttonWidth, buttonHeight); // Rules button

        float exitButtonY = startY - 25; // Calculate y position for Exit button
        batch.draw(exitButtonTexture, startX + 8, exitButtonY, buttonWidth, buttonHeight); // Exit button

        batch.end();

        if (cooldownTimer <= 0 && Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Convert Y


            // Play button clicked
            if (touchX >= startX && touchX <= startX + buttonWidth
                && touchY >= playButtonY && touchY <= playButtonY + buttonHeight) {
                game.setScreen(new HomeScreen(game));  // Switch to HomeScreen
                cooldownTimer = inputCooldown;  // Reset the cooldown timer after screen switch
            }

            // Exit button clicked
            if (touchX >= startX && touchX <= startX + buttonWidth
                && touchY >= exitButtonY && touchY <= exitButtonY + buttonHeight) {
                Gdx.app.exit();  // Close the application
            }

           //  Rules button clicked
        if (touchX >= startX && touchX <= startX + buttonWidth
            && touchY >= rulesButtonY && touchY <= rulesButtonY + buttonHeight) {
            game.setScreen(new RuleScreen(game));  // Switch to RuleScreen
            cooldownTimer = inputCooldown;  // Reset the cooldown timer after screen switch
        }
        }
    }

    @Override
    public void resize(int width, int height) {
        updateButtonPositions();
    }

    private void updateButtonPositions() {
        startX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        // Increased the downward offset to move buttons lower
        startY = (Gdx.graphics.getHeight() - (buttonHeight * 3 + 20)) / 2 - 80; // Centering buttons lower by moving them further down
    }

    @Override
    public void hide() {
        batch.dispose();
        background.dispose();
        font.dispose();
        // Dispose of button textures
        playButtonTexture.dispose();
        rulesButtonTexture.dispose();
        exitButtonTexture.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
