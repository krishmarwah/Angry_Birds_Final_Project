package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class RuleScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Texture ruleImage;

    public RuleScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        ruleImage = new Texture("game_rules.png");  // Load the rule image
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);  // Clear the screen with black
        batch.begin();

        // Get the screen dimensions and the rule image dimensions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float imgWidth = ruleImage.getWidth();
        float imgHeight = ruleImage.getHeight();

        // Calculate the scaling factor to fit the image within the screen without any cropping
        float scale = Math.min(screenWidth / imgWidth, screenHeight / imgHeight);
        float scaledWidth = imgWidth * scale;
        float scaledHeight = imgHeight * scale;

        // Adjust the position to center the image on the screen
        float x = (screenWidth - scaledWidth) / 2;
        float y = (screenHeight - scaledHeight) / 2;

        // Draw the scaled rule image
        batch.draw(ruleImage, x, y, scaledWidth, scaledHeight);

        batch.end();

        // Check if the user clicks in the top rows of the rule image
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Convert Y to match screen coordinates

            // Define the "top rows" as the upper portion of the scaled rule image (e.g., top 25% of the image)
            float topRowHeight = scaledHeight / 4;  // Adjust this fraction to define what "top rows" mean
            float topRowY = y + (scaledHeight - topRowHeight);  // Y position of the top rows

            if (touchX >= x && touchX <= x + scaledWidth && touchY >= topRowY && touchY <= y + scaledHeight) {
                // If the user clicks within the top rows, go back to the StartScreen
                game.setScreen(new StartScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {
        batch.dispose();
        ruleImage.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
