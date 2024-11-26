package Angry.Birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
//    private int score;
//    private int currentLevel; // Track the current level
//    private List<Pig> pigs; // List of pigs in the game
//    private List<WoodMaterial> woodBlocks; // List of wood blocks
//    private List<GlassMaterial> glassBlocks; // List of glass blocks
//    private List<StoneMaterial> stoneBlocks; // List of stone blocks
//    private Bird currentBird; // The currently active bird
//
//    public GameHandler() {
//        pigs = new ArrayList<>();
//        woodBlocks = new ArrayList<>();
//        glassBlocks = new ArrayList<>();
//        stoneBlocks = new ArrayList<>();
//    }
//
//    public void saveGameState() {
//        GameState gameState = new GameState();
//
//        // Set the current game state information
//        gameState.score = this.score;
//        gameState.level = this.currentLevel;
//
//        // Add pigs to state
//        for (Pig pig : this.pigs) {
//            GameState.PigState pigState = new GameState.PigState();
//            pigState.x = pig.getX();
//            pigState.y = pig.getY();
//            pigState.durability = pig.health; // Assuming health is a public field
//            gameState.pigs.add(pigState);
//        }
//
//        // Add wood blocks to state
//        for (WoodMaterial wood : this.woodBlocks) {
//            GameState.BlockState blockState = new GameState.BlockState();
//            blockState.x = wood.getX();
//            blockState.y = wood.getY();
//            blockState.type = "Wood"; // Specify the type based on the object
//            blockState.durability = wood.durability; // Assuming durability is public in WoodMaterial
//            gameState.blocks.add(blockState);
//        }
//
//        // Add glass blocks to state
//        for (GlassMaterial glass : this.glassBlocks) {
//            GameState.BlockState blockState = new GameState.BlockState();
//            blockState.x = glass.getX();
//            blockState.y = glass.getY();
//            blockState.type = "Glass";
//            blockState.durability = glass.durability; // Assuming durability is public in GlassMaterial
//            gameState.blocks.add(blockState);
//        }
//
//        // Add stone blocks to state
//        for (StoneMaterial stone : this.stoneBlocks) {
//            GameState.BlockState blockState = new GameState.BlockState();
//            blockState.x = stone.getX();
//            blockState.y = stone.getY();
//            blockState.type = "Stone";
//            blockState.durability = stone.durability; // Assuming durability is public in StoneMaterial
//            gameState.blocks.add(blockState);
//        }
//
//        // Save the current bird state if it exists
//        if (this.currentBird != null) {
//            GameState.BirdState birdState = new GameState.BirdState();
//            birdState.type = this.currentBird.getClass().getSimpleName(); // Get bird type name
//            birdState.x = currentBird.getX();
//            birdState.y = currentBird.getY();
//            gameState.currentBird = birdState;
//        }
//
//        // Serialize and save to JSON
//        Json json = new Json();
//        String jsonString = json.toJson(gameState);
//        FileHandle file = Gdx.files.local("gameSave.json");
//        file.writeString(jsonString, false);
//    }
//
//    public void loadGameState() {
//        FileHandle file = Gdx.files.local("gameSave.json");
//        if (!file.exists()) {
//            return; // Or handle the case where the save file does not exist
//        }
//
//        Json json = new Json();
//        GameState gameState = json.fromJson(GameState.class, file.readString());
//
//        // Restore the game state
//        this.score = gameState.score;
//        this.currentLevel = gameState.level;
//
//        // Clear existing pigs and blocks
//        this.pigs.clear();
//        this.woodBlocks.clear();
//        this.glassBlocks.clear();
//        this.stoneBlocks.clear();
//
//        // Restore pigs
//        for (GameState.PigState pigState : gameState.pigs) {
//            Pig pig = new BasicPig(pigState.x, pigState.y); // Example for creating pigs
//            pig.health = pigState.durability; // Set durability
//            this.pigs.add(pig);
//        }
//
//        // Restore wood blocks
//        for (GameState.BlockState blockState : gameState.blocks) {
//            if ("Wood".equals(blockState.type)) {
//                WoodMaterial wood = new WoodMaterial(world, blockState.x, blockState.y);
//                wood.durability = blockState.durability; // Set durability for wood blocks
//                this.woodBlocks.add(wood);
//            } else if ("Glass".equals(blockState.type)) {
//                GlassMaterial glass = new GlassMaterial(world, blockState.x, blockState.y);
//                glass.durability = blockState.durability; // Set durability for glass blocks
//                this.glassBlocks.add(glass);
//            } else if ("Stone".equals(blockState.type)) {
//                StoneMaterial stone = new StoneMaterial(world, blockState.x, blockState.y);
//                stone.durability = blockState.durability; // Set durability for stone blocks
//                this.stoneBlocks.add(stone);
//            }
//        }
//
//        // Restore the current bird if exists
//        if (gameState.currentBird != null) {
//            switch (gameState.currentBird.type) {
//                case "RedBird":
//                    currentBird = new RedBird(gameState.currentBird.x, gameState.currentBird.y);
//                    break;
//                case "BlackBird":
//                    currentBird = new BlackBird(gameState.currentBird.x, gameState.currentBird.y);
//                    break;
//                case "BlueBird":
//                    currentBird = new BlueBird(gameState.currentBird.x, gameState.currentBird.y);
//                    break;
//                // Add additional bird types here
//                default:
//                    currentBird = null; // Or a default bird if needed
//            }
//        }
//    }
//    // Additional methods for score management, current level tracking, etc., can be added here
}
