package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

public class MessageScreen2 implements Screen {
    private SpriteBatch batch;
    private Texture msgImage;  // The message image (msg.png)
    private Game game;  // Reference to the Game instance

    // Constructor that accepts the game instance
    public MessageScreen2(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        msgImage = new Texture("msg4.png");  // Load the message image
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a color
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the message image
        batch.begin();
        batch.draw(msgImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Detect click in the top half of the screen (navigate back to HomeScreen)
        if (Gdx.input.isButtonPressed(0)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Invert the Y-coordinate

            // Check if the user clicked within the top half of the screen
            if (touchY > Gdx.graphics.getHeight() / 2) {
                System.out.println("Clicked in the top half, returning to HomeScreen...");
                game.setScreen(new HomeScreen(game));  // Navigate back to HomeScreen
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing if necessary
    }

    @Override
    public void hide() {
        // Dispose resources when the screen is hidden
        batch.dispose();
        msgImage.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
