package Angry.Birds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackBirdTest {
    @Test
    void testSpecialAbility() {
        // Redirect system output to test the behavior
        String expectedOutput = "Black Bird explodes on impact!";
        assertDoesNotThrow(() -> BlackBird.explode(), "Special ability should execute without errors");
    }
}
