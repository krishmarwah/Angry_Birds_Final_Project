package Angry.Birds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlueBirdTest {
    @Test
    void testSpecialAbility() {
        // Test the extracted static method
        assertDoesNotThrow(() -> BlueBird.split(), "Special ability should execute without errors");
    }
}
