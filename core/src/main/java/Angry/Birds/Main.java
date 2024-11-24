package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        // Initialize the SpriteBatch for rendering
        batch = new SpriteBatch();

        // Set the initial screen when the game starts
        setScreen(new OpeningScreen(this));  // Start with the OpeningScreen
    }

    @Override
    public void render() {
        // Delegate the render method to the currently active screen
        super.render();
    }

    @Override
    public void dispose() {
        // Dispose of the SpriteBatch to free up resources
        batch.dispose(); // Dispose of the SpriteBatch

        // Call the superclass dispose method to dispose of the screens
        super.dispose();
    }
}
