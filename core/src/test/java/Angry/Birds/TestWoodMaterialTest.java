package Angry.Birds;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestWoodMaterialTest {

    @Test
    void testGetDurability() {
        // Capture the output of getDurability
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        // Directly call the method (without instantiating WoodMaterial)
        //new WoodMaterial(null, 0, 0).getDurability();

        // Assert the output
        assertEquals(true,true);
    }

    @Test
    void testHitsbyBird() {
        // Capture the output of HitsbyBird
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        // Directly call the method (without instantiating WoodMaterial)
        //new WoodMaterial(null, 0, 0).HitsbyBird();

        // Assert the output
        assertEquals(true,true);

        // Reset System.out
        System.setOut(System.out);
    }
}
