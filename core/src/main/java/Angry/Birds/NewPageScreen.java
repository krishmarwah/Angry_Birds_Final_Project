package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class NewPageScreen implements Screen {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;  // ShapeRenderer to draw shapes
    private Game game;  // Reference to Main (the Game instance)

    private Texture newPageBackground;  // Texture for the background
    private Texture playButtonTexture;  // Texture for the "Play" button
    private Texture backButtonTexture;   // Texture for the "Back" button
    private BitmapFont font;  // Font for rendering text

    // Constructor to accept the Main instance
    public NewPageScreen(Game game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();  // Initialize BitmapFont
        newPageBackground = new Texture("new_image.png");  // Load the background image
        playButtonTexture = new Texture("play.png");  // Load the play button image
        backButtonTexture = new Texture("back.png");  // Load the back button image
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);  // Set background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background image
        batch.begin();
        batch.draw(newPageBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the "Play" button image in the bottom-right
        float playButtonX = Gdx.graphics.getWidth() - playButtonTexture.getWidth() - 20;  // 20 padding from the right
        float playButtonY = 20;  // Slight padding from the bottom
        batch.draw(playButtonTexture, playButtonX, playButtonY);  // Draw play button image

        // Draw the "Back" button image in the bottom-left
        float backButtonX = 20;  // Padding from the left
        float backButtonY = 20;  // Slight padding from the bottom
        batch.draw(backButtonTexture, backButtonX, backButtonY);  // Draw back button image

        // End batch for drawing
        batch.end();

        // Check for click events
        if (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Invert Y-coordinate

            // Bottom-right (Play button area check)
            if (touchX >= playButtonX && touchX <= playButtonX + playButtonTexture.getWidth()
                && touchY >= playButtonY && touchY <= playButtonY + playButtonTexture.getHeight()) {
                game.setScreen(new GameScreen(game));  // Switch to GameScreen when Play button is clicked
            }

            // Bottom-left (Back button area check)
            if (touchX >= backButtonX && touchX <= backButtonX + backButtonTexture.getWidth()
                && touchY >= backButtonY && touchY <= backButtonY + backButtonTexture.getHeight()) {
                game.setScreen(new HomeScreen(game));  // Switch to HomeScreen when Back button is clicked
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {
        batch.dispose();
        shapeRenderer.dispose();  // Dispose ShapeRenderer
        font.dispose();  // Dispose BitmapFont
        newPageBackground.dispose();  // Dispose background texture
        playButtonTexture.dispose();  // Dispose play button texture
        backButtonTexture.dispose();  // Dispose back button texture
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
