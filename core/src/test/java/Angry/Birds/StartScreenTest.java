package Angry.Birds;

import com.badlogic.gdx.Game;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StartScreenTest {

    @Test
    public void testStartScreenConstructor() {
        // Create a simple subclass of Game for testing purposes
        Game mockGame = new Game() {
            @Override
            public void create() {
                // No need to implement, it's just for testing
            }
        };

        // Create a new StartScreen using the real Game instance
        StartScreen startScreen = new StartScreen(mockGame);

        // Assert that the game variable in the StartScreen is not null
        assertNotNull(startScreen, "StartScreen object should not be null");

        // Assert that the game reference passed to the constructor is not null
        assertNotNull(startScreen.game, "Game reference should not be null");
    }
}


