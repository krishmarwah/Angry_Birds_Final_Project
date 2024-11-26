package Angry.Birds;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

// Class to represent the game's state for serialization
public class GameState {
    public int score; // Player's score
    public int level; // Current game level
    public List<PigState> pigs = new ArrayList<>(); // State of all pigs
    public List<BlockState> blocks = new ArrayList<>(); // State of all blocks
    public BirdState currentBird; // State of the current bird

    // Inner class to represent the state of a pig
    public static class PigState {
        public float x; // X position
        public float y; // Y position
        public int durability; // Durability of the pig
    }

    // Inner class to represent the state of a block
    public static class BlockState {
        public float x; // X position
        public float y; // Y position
        public String type; // Type of block ("Wood", "Glass", "Stone", etc.)
        public int durability; // Durability of the block
    }

    // Inner class to represent the state of a bird
    public static class BirdState {
        public String type; // Type of bird (e.g., "RedAngryBird")
        public float x; // X position
        public float y; // Y position
    }

    // Method to serialize the GameState object to JSON
    public String serialize() {
        Json json = new Json(); // Create a new Json object
        return json.toJson(this); // Convert the current GameState to JSON string
    }

    // Method to deserialize a JSON string back into a GameState object
    public static GameState deserialize(String jsonString) {
        Json json = new Json(); // Create a new Json object
        return json.fromJson(GameState.class, jsonString); // Convert JSON string back to GameState
    }
}
