package Angry.Birds;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStateTest {

    @Test
    public void testGameStateSerialization() {
        GameState gameState = new GameState();
        gameState.score = 100;
        gameState.level = 1;

        String json = gameState.serialize();
        GameState deserializedGameState = GameState.deserialize(json);

        // Check if the deserialized state matches the original state
        assertEquals(gameState.score, deserializedGameState.score);
        assertEquals(gameState.level, deserializedGameState.level);
    }
}
