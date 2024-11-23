package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Game;

public class HomeScreen implements Screen {
    private SpriteBatch batch;
    private Texture homeBackground;
    private Game game;  // Reference to the Main game instance

    private Stage stage;
    private ImageButton levelButton;  // Single button for the level
    private ImageButton messageButton;  // Message button

    // Constructor that accepts the game instance
    public HomeScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        homeBackground = new Texture("home_bg.png");  // Load the home screen background

        // Initialize the stage and add buttons
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load button images
        Texture levelTexture = new Texture(Gdx.files.internal("level.png"));  // Level button image
        Texture messageTexture = new Texture(Gdx.files.internal("Msg.png"));  // Message button image

        // Create ImageButtons using TextureRegionDrawable
        levelButton = new ImageButton(new TextureRegionDrawable(levelTexture));
        messageButton = new ImageButton(new TextureRegionDrawable(messageTexture));  // Create message button

        // Adjust button width and height to fit the screen better (adjust these values as needed)
        float buttonWidth = levelTexture.getWidth() / 1.5f;  // Scale width down by 1.5
        float buttonHeight = levelTexture.getHeight() / 1.5f;  // Scale height down by 1.5

        // Set positions for the buttons with slight adjustments
        float horizontalPadding = 50;  // Increased padding to move the level button further left
        float verticalPadding = 15;     // Move level button down by 15px

        // Position for the level button at the top right corner with adjustments
        float xPosition = Gdx.graphics.getWidth() - buttonWidth - horizontalPadding;  // Move left
        float yPosition = Gdx.graphics.getHeight() - buttonHeight - verticalPadding;  // Move down

        // Set the position for the level button
        levelButton.setPosition(xPosition, yPosition);

        // Position the message button in the bottom-left corner (with some padding)
        messageButton.setPosition(20, 20);  // 20px padding from the left and bottom edges

        // Add buttons to the stage
        stage.addActor(levelButton);  // Add the level button to the stage
        stage.addActor(messageButton);  // Add message button to the stage

        // Add button listeners
        levelButton.addListener(event -> {
            if (levelButton.isPressed()) {
                game.setScreen(new NewPageScreen(game));  // Navigate to NewPageScreen
            }
            return true;
        });

        messageButton.addListener(event -> {
            if (messageButton.isPressed()) {
                game.setScreen(new MessageScreen(game));  // Navigate to MessageScreen on click
            }
            return true;
        });
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a color
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the home screen background
        batch.begin();
        batch.draw(homeBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw buttons on the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Dispose resources when the screen is hidden
        batch.dispose();
        homeBackground.dispose();
        stage.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        homeBackground.dispose();
        stage.dispose();
    }
}
