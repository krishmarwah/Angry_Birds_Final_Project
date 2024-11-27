package Angry.Birds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestBasicPigTest {

    private TestBasicPig pig;

    @BeforeEach
    void setUp() {
        // Create a BasicPig instance without LibGDX dependencies
        pig = new TestBasicPig();
    }

    @Test
    void testInitialDurability() {
        assertEquals(1, pig.getDurability(), "Initial durability should be 1");
    }

    @Test
    void testTakeHit() {
        // Initial durability should be 1
        pig.takeHit();

        // After one hit, durability should be 0
        assertEquals(0, pig.getDurability(), "Durability should decrease after hit");

        // The pig should be marked for destruction
        assertTrue(pig.isMarkedForDestruction(), "Pig should be marked for destruction after durability reaches 0");
    }

    @Test
    void testHandleCollisionWithRedBird() {
        pig.handleCollision("RedAngryBird");

        // After the RedAngryBird collision, durability should be decreased by 1
        assertEquals(0, pig.getDurability(), "Durability should decrease by 1 for RedAngryBird");

        // The pig should be marked for destruction
        assertTrue(pig.isMarkedForDestruction(), "Pig should be marked for destruction after collision with RedAngryBird");
    }

    @Test
    void testHandleCollisionWithBlackBird() {
        pig.handleCollision("BlackAngryBird");

        // After the BlackAngryBird collision, durability should be decreased by 2
        assertEquals(-1, pig.getDurability(), "Durability should decrease by 2 for BlackAngryBird");

        // The pig should still be marked for destruction
        assertTrue(pig.isMarkedForDestruction(), "Pig should be marked for destruction after collision with BlackAngryBird");
    }

    @Test
    void testHandleCollisionWithBlueBird() {
        pig.handleCollision("BlueAngryBird");

        // After the BlueAngryBird collision, durability should be decreased by 5
        assertEquals(-4, pig.getDurability(), "Durability should decrease by 5 for BlueAngryBird");

        // The pig should still be marked for destruction
        assertTrue(pig.isMarkedForDestruction(), "Pig should be marked for destruction after collision with BlueAngryBird");
    }

    @Test
    void testDispose() {
        // Check that pig is not disposed yet
        assertFalse(pig.isDisposed(), "Pig should not be disposed initially");

        // Call dispose
        pig.dispose();

        // After calling dispose, the pig should be marked as disposed
        assertTrue(pig.isDisposed(), "Pig should be disposed after calling dispose method");

        // Calling dispose again should have no effect
        pig.dispose();
        assertTrue(pig.isDisposed(), "Pig should not be disposed again");
    }

    @Test
    void testMarkedForDestruction() {
        // Call takeHit until destruction
        pig.takeHit();

        // After hitting once, the pig should be marked for destruction
        assertTrue(pig.isMarkedForDestruction(), "Pig should be marked for destruction after durability reaches 0");
    }
}
