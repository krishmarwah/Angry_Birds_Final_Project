package Angry.Birds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

public class WinPage implements Screen {
    private int level=0;
    private SpriteBatch batch;
    private Texture winImage;
    private Texture nextWordTexture; // Texture for the next word button
    private Game game;

    private float buttonWidth = 200;  // Width of the clickable region (around the middle)
    private float buttonHeight = 50;  // Height of the clickable region
    private float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;  // X-position (centered horizontally)
    private float buttonY = 30;  // Adjusted Y-position (lowered from 50 to 30)

    private float inputCooldown = 1.5f;  // Increased cooldown to 1.5 seconds
    private float timeSinceLastInput = 0f;  // Timer to track input cooldown
    private boolean canClick = true;  // Flag to check if the action can be performed

    public WinPage(Game game,int level) {
        this.game = game;
        this.level=level;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        winImage = new Texture("win.png");  // Load the win image
        nextWordTexture = new Texture("next_word.png");  // Load the next word button texture
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);  // Set background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the win image
        batch.begin();
        batch.draw(winImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the next word button at the bottom middle
        float nextWordX = (Gdx.graphics.getWidth() - 128) / 2; // Center horizontally
        float nextWordY = buttonY; // Position the button lower
        batch.draw(nextWordTexture, nextWordX, nextWordY, 158, 64); // Draw next word button

        batch.end();

        // Update cooldown timer
        if (timeSinceLastInput < inputCooldown) {
            timeSinceLastInput += delta;
        } else {
            canClick = true;  // Allow input after cooldown expires
        }

        // Check for click action only if the cooldown has expired
        if (canClick && Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Invert Y-axis

            // Check if the click is inside the next word button bounds
            if (touchX >= nextWordX && touchX <= nextWordX + 128 &&
                touchY >= nextWordY && touchY <= nextWordY + 64) {
                // Transition to the NewPageScreen
                System.out.println("Next word button clicked");
                if (level == 0) {
                    game.setScreen(new NewPageScreen(game)); // Replace with your NewPageScreen instance

                }
                else if(level==1){
                    game.setScreen(new NewPageScreen1(game));
                }
                else if(level==2){
                    game.setScreen(new NewPageScreen2(game));
                }
            }

        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {
        batch.dispose();
        winImage.dispose();
        nextWordTexture.dispose(); // Dispose of next word button texture
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}
