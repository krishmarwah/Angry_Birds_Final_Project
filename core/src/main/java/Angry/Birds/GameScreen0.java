package Angry.Birds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameScreen0 implements Screen, Serializable {

    private ArrayList<Vector2> woodBlockPositions;  // List to store wood block positions
    private ArrayList<Vector2> pigPositions;// List to store wood block positions
    Queue<String> birdarray;


    private transient Game game;
    private transient SpriteBatch batch;
    private transient ShapeRenderer shapeRenderer; // For drawing shapes like the rope and trajectory
    private transient Texture redBirdTexture;
    private transient Texture blackBirdTexture;
    private transient Texture blueBirdTexture;
    private transient Texture background;
    private transient Texture catapult;
    private transient Texture pauseButton;
    private transient boolean isPaused = false;

    private transient Texture basicPigTexture;
    private transient List<Pig> pigs; // Store all pigs


    private transient World world; // Box2D world
    private transient Body birdBody; // Bird physics body
    private transient Body groundBody; // Ground physics body

    private transient boolean dragging = false; // To check if bird is being dragged
    private transient Vector2 initialPosition; // Bird's initial position
    private transient Vector2 launchVelocity = new Vector2(); // Initial velocity to be applied

    private transient Vector2 catapultAnchor; // Anchor point of the catapult

    private transient List<WoodMaterial> woodBlocks;// List to store wood blocks
    private transient List<BasicPig> pig;
    private transient Queue<String> birdQueue; // Queue to manage birds in sequence
    private transient boolean isBirdActive = false;

    private transient Vector2 ropeEnd; // The dynamic endpoint of the rope during dragging
    private transient float maxStretch = 50f; // Maximum stretch distance
    private transient Vector2 leftAnchor; // Left end of the catapult
    private transient Vector2 rightAnchor; // Right end of the catapult

    private transient int score = 0;


    public GameScreen0(Game game) {
        this.game = game;
        pigs = new ArrayList<>();

        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        // Load textures
        redBirdTexture = new Texture("red_bird.png");
        blackBirdTexture = new Texture("black_bird.png");
        blueBirdTexture = new Texture("blue_bird.png");
        background = new Texture("bg.png");
        catapult = new Texture("catapult.png");
        basicPigTexture=new Texture("basic_pig.png");
        pauseButton = new Texture("pause.png");



        // Initialize Box2D world with gravity
        world = new World(new Vector2(0, -15f), true);

        // Create the ground body
        createGround();

        // Initialize the catapult anchor position
        catapultAnchor = new Vector2(100, 200); // Fixed position for the catapult
        ropeEnd = new Vector2(catapultAnchor); // Initially at the catapult anchor
        leftAnchor = new Vector2(catapultAnchor.x - 20, catapultAnchor.y + 30);  // Adjust for left side
        rightAnchor = new Vector2(catapultAnchor.x + 20, catapultAnchor.y + 30); // Adjust for right side



        // Initialize bird queue
        birdQueue = new LinkedList<>();
        birdQueue.add("RedAngryBird");
        birdQueue.add("RedAngryBird");
        birdQueue.add("RedAngryBird");

        // Spawn the first bird
        spawnNextBird();

        // Initialize wood blocks and create a level
        woodBlocks = new ArrayList<>();
        pig=new ArrayList<>();
        createLevel();

        setupCollisionListener();
    }

    private void saveWoodBlockPositions() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("wood_positions.dat"))) {
            List<Vector2> positions = new ArrayList<>();
            for (WoodMaterial wood : woodBlocks) {
                positions.add(wood.getBody().getPosition());
            }
            out.writeObject(positions);
            System.out.println("Wood block positions saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameState() {
        try {
            // Open the saved game state file
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\save_game_state.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Load wood block positions from saved state
            woodBlockPositions = (ArrayList<Vector2>) objectInputStream.readObject();
            pigPositions = (ArrayList<Vector2>) objectInputStream.readObject();
            birdarray=(Queue<String>) objectInputStream.readObject();


            // Close the file stream after reading
            objectInputStream.close();
            fileInputStream.close();

            Gdx.app.log("Load", "Game state loaded successfully.");

            // Clear the existing wood blocks array
            woodBlocks.clear();

            // Create wood blocks based on loaded positions
            for (Vector2 position : woodBlockPositions) {
                woodBlocks.add(new WoodMaterial(world, position.x, position.y));
            }

            pigs.clear();

            // Create pigs based on loaded positions
            for (Vector2 position : pigPositions) {
                pig.add(new BasicPig(world, position.x, position.y)); // Assuming Pig is your class for pigs
            }

            birdQueue.clear();
            for(String position : birdarray){
                birdQueue.add(position);
            }

        } catch (Exception e) {
            Gdx.app.log("Error", "Error loading game state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Vector2> getWoodBlockPositions() {
        List<Vector2> positions = new ArrayList<>();
        for (WoodMaterial wood : woodBlocks) {
            if (wood.getBody() != null) {
                positions.add(wood.getBody().getPosition().cpy()); // Get and copy the position to avoid modifications
            }
        }
        return positions;
    }
    public List<Vector2> getpigPositions() {
        List<Vector2> positions = new ArrayList<>();
        for (BasicPig pi : pig) {
            if (pi.getBody() != null) {
                positions.add(pi.getBody().getPosition().cpy()); // Get and copy the position to avoid modifications
            }
        }
        return positions;
    }

    public Queue<String> getBirdQueue() {
        Queue<String> positions = new LinkedList<>();
        for (String pi : birdQueue) {
            System.out.println(pi);
            positions.add(pi);
        }
        return positions;
    }

    private void loadWoodBlockPositions() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("wood_positions.dat"))) {
            List<Vector2> positions = (List<Vector2>) in.readObject();
            for (int i = 0; i < woodBlocks.size(); i++) {
                woodBlocks.get(i).getBody().setTransform(positions.get(i), woodBlocks.get(i).getBody().getAngle());
            }
            System.out.println("Wood block positions loaded!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void pauseGame() {
        isPaused = true;
        // Stop animations, timers, or any active processes
    }

    public void resumeGame() {
        isPaused = false;
        // Resume animations, timers, etc.
    }

    private void createGround() {
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(0, 75); // Ground at y = 0
        groundDef.type = BodyDef.BodyType.StaticBody;

        groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(Gdx.graphics.getWidth(), 10); // Ground width and thickness

        groundBody.createFixture(groundShape, 0.0f);

        groundShape.dispose();
    }

    private void saveGameState() {
        try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\DELL\\Desktop\\save_game_state.txt");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            // Save the positions of objects in GameScreen0 (example for level 0)
            if ( this != null) {
                objectOut.writeObject(this.getWoodBlockPositions());// Save wood block positions
                objectOut.writeObject(this.getpigPositions());
                objectOut.writeObject(this.getBirdQueue());
            }

            Gdx.app.log("Save", "Game state saved successfully!");
        } catch (Exception ex) {
            Gdx.app.log("Save", "Error saving game state: " + ex.getMessage());
        }


    }



    private void setupCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Body bodyA = fixtureA.getBody();
                Body bodyB = fixtureB.getBody();

                // Check for bird-ground collision (keep this)
                if ((bodyA == birdBody || bodyB == birdBody) && (bodyA == groundBody || bodyB == groundBody)) {
                    isBirdActive = false;
                    spawnNextBird();
                } else {

                }

                if(bodyA.equals(birdBody) || bodyB.equals(birdBody)) {
                    Body other = (bodyA == birdBody) ? bodyB : bodyA;
                    for (WoodMaterial wood : woodBlocks) {
                        if (wood.getBody() == other) {
                            wood.handleCollision(getBirdType(birdBody));
                            updateScore(50);
                            break;
                        }
                    }
                }

                for (BasicPig pig : pig) {
                    Body pigBody = pig.getBody();


                    // Bird-pig collision
                    if ((bodyA == birdBody && bodyB == pigBody) ) {
                        String birdType = bodyA.getUserData() != null ? birdBody.getUserData().toString() : "Unknown";
                        pig.handleCollision(birdType);
                        if(pig.isMarkedForDestruction()) {
                            updateScore(500);
                        } // Award points for hitting a pig
                    }else if(bodyB == birdBody && bodyA == pigBody){
                        String birdType = bodyB.getUserData() != null ? birdBody.getUserData().toString() : "Unknown";
                        pig.handleCollision(birdType);
                        if(pig.isMarkedForDestruction()) {
                            updateScore(500);
                        }
                    }

                    // Ground-pig collision
                    if ((bodyA == groundBody && bodyB == pigBody) ||
                        (bodyB == groundBody && bodyA == pigBody)) {
                        pig.handleCollision();
                        updateScore(500); // Award points for hitting a pig

                    }
                }
            }





            private String getBirdType(Body bird) {
                return bird.getUserData() != null ? bird.getUserData().toString() : "Unknown";
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void updateScore(int points) {
        score += points; // Update score
        if (score >= 500) {
            goToWinPage(); // Transition to win screen if score >= 500
        }
    }
    private void goToWinPage() {
        // Logic to transition to the win page
        flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state.txt");
        flushDataFromFile("C:\\Users\\DELL\\Desktop\\level_no.txt");
        saveLevel("C:\\Users\\DELL\\Desktop\\level_no.txt");
        LosePage.keys=1;
        HomeScreen.level=1;

        game.setScreen(new WinPage(game,1)); // Assuming WinPageScreen is implemented
    }

    public static void saveLevel(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(1)); // Convert level to string and write
            System.out.println("Level saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save the level.");
        }
    }

    public static void flushDataFromFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            // Opening in write mode without writing anything clears the file
            System.out.println("File contents cleared: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to clear the file: " + filePath);
        }
    }

    private void checkGameEnd() {
        if (score < 500) {
            if(LosePage.keys==0){
            flushDataFromFile("C:\\Users\\DELL\\Desktop\\save_game_state.txt");
            }else{
                saveGameState();
            }
            goToLosePage(); // Transition to the lose page
        }
    }
    private void goToLosePage() {
        game.setScreen(new LosePage(game,0,this,null,null)); // Assuming LosePage class is implemented
    }





    private boolean birdInMotion() {
        if (birdBody == null) return false;

        // Check if the active bird has stopped moving or fallen below the ground
        boolean isResting = birdBody.getLinearVelocity().len2() < 0.01f && birdBody.getAngularVelocity() < 0.01f;
        boolean isBelowGround = birdBody.getPosition().y < 50; // Adjust for ground height

        return !(isResting || isBelowGround);
    }


    private void spawnNextBird() {
        if (birdQueue.isEmpty() || isBirdActive) {
            checkGameEnd();
            return;
        }

        Gdx.app.postRunnable(() -> {
            String birdType = birdQueue.poll();
            createBird(birdType); // Create a new bird
            ropeEnd.set(catapultAnchor); // Reset rope position
            initialPosition = birdBody.getPosition().cpy();
            isBirdActive = true; // Mark the bird as active
        });
    }

    public boolean isFileEmpty(String filePath) {
        File file = new File(filePath);

        // Check if the file exists and if its length is greater than 0
        return file.exists() && file.length() == 0;
    }




    private void createBird(String birdType) {
        BodyDef birdDef = new BodyDef();
        birdDef.type = BodyDef.BodyType.DynamicBody;
        birdDef.position.set(catapultAnchor.x+20, catapultAnchor.y-50); // Initial position at the catapult

        birdBody = world.createBody(birdDef);

        CircleShape birdShape = new CircleShape();
        birdShape.setRadius(12); // Radius of the bird

        FixtureDef birdFixture = new FixtureDef();
        birdFixture.shape = birdShape;
        birdFixture.density = 1.0f; // Mass properties
        birdFixture.friction = 0.5f;
        birdFixture.restitution = 0.6f; // Bounce effect



        birdBody.createFixture(birdFixture);
        birdShape.dispose();
        birdBody.setUserData(birdType);


        birdBody.setAwake(false);
    }

    public static boolean hasMoreThanTenCharacters(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            int count = 0;
            int character;
            while ((character = reader.read()) != -1) {
                count++;
                if (count > 10) {
                    return true; // Return true as soon as we find more than 10 characters
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return false; // Return false if the file has 10 or fewer characters
    }



    private void createLevel() {
          if(!isFileEmpty("C:\\Users\\DELL\\Desktop\\save_game_state.txt") && hasMoreThanTenCharacters("C:\\Users\\DELL\\Desktop\\save_game_state.txt")){
              loadGameState();
          }else {
              float blockWidth = 50;
              float blockHeight = 20;
              float startX = Gdx.graphics.getWidth() / 2 - blockWidth * 1.5f; // Centered
              float startY = 100;
              int numBlocksPerRow = 3;
              int numBlocksPerColumn = 4;

              // Create the base layer
              for (int j = 0; j < numBlocksPerRow; j++) {
                  float x = startX + j * (blockWidth + 1) + 150;
                  float y = startY;
                  woodBlocks.add(new WoodMaterial(world, x, y));
              }

              // Create the middle layer (with the pig)
              for (int j = 0; j < numBlocksPerRow; j++) {
                  float x = startX + j * (blockWidth + 1) + 150;
                  float y = startY + blockHeight + 1;

                  if (j == 1) { // Place the pig in the center
                      pig.add(new BasicPig(world, x + blockWidth / 2, y + blockHeight / 2));
                  } else {
                      woodBlocks.add(new WoodMaterial(world, x, y));
                  }
              }

              // Create the top layer (optional)
              for (int j = 0; j < numBlocksPerRow; j++) {
                  if (j == numBlocksPerRow - 1) {
                      continue;
                  }
                  float x = startX + j * (blockWidth + 1) + 150;
                  float y = startY + 2 * (blockHeight + 1);
                  woodBlocks.add(new WoodMaterial(world, x, y));
              }
          }

    }



    private void handleInput() {
        if (!isBirdActive) return; // Ignore input if no active bird

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (!dragging && touchPoint.dst(birdBody.getPosition()) < 20) {
                dragging = true; // Begin dragging
            }

            if (dragging) {
                Vector2 adjustedPosition = touchPoint.cpy();
                if (adjustedPosition.dst(catapultAnchor) > maxStretch) {
                    adjustedPosition = catapultAnchor.cpy().add(
                        adjustedPosition.sub(catapultAnchor).nor().scl(maxStretch)
                    );
                }
                ropeEnd.set(adjustedPosition); // Update rope's endpoint
                birdBody.setTransform(adjustedPosition, birdBody.getAngle());
            }
        } else if (dragging) {
            dragging = false;

            // Launch the bird
            Vector2 releasePosition = ropeEnd.cpy();
            launchVelocity.set(catapultAnchor.cpy().sub(releasePosition).scl(2f));
            birdBody.setAwake(true);
            birdBody.setLinearVelocity(launchVelocity);

            launchBird();

            // Reset rope position
            ropeEnd.set(catapultAnchor);
        }
    }

    private void launchBird() {
        if (isBirdActive) {
            // Launch logic...
            birdBody.setAwake(true);

            // After launching the bird, convert blocks to DynamicBody
            for (WoodMaterial block : woodBlocks) {
                block.makeDynamic(); // Convert to dynamic to react to gravity and collisions
            }
        }
    }



    @Override
    public void render(float delta) {
        if (isPaused) return;
        // Update the Box2D world
        world.step(1 / 60f, 6, 2);
        destroyMarkedBodies();

        // Check if the active bird is out of bounds
        if (birdBody != null) {
            float birdX = birdBody.getPosition().x;
            float birdY = birdBody.getPosition().y;

            if (birdX < -50 || birdX > Gdx.graphics.getWidth() + 50 || birdY < 50) {
                isBirdActive = false;
                spawnNextBird(); // Spawn the next bird
            }
        }

        // Clear the screen
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the click is inside the pause button bounds
            if (x >= 10 && x <= 10 + 64 && y >= Gdx.graphics.getHeight() - 74 && y <= Gdx.graphics.getHeight() - 10) {
                pauseGame();
                game.setScreen(new PauseScreen(game,0,this,null,null));
            }
        }

        // Handle input for dragging and launching
        handleInput();

        // Begin SpriteBatch to draw textures
        batch.begin();



        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(catapult, catapultAnchor.x, catapultAnchor.y - 75, 60, 40);
        batch.draw(pauseButton, 10, Gdx.graphics.getHeight() - 74, 64, 64);
        // Draw the bird
        String birdType = birdBody.getUserData() != null ? birdBody.getUserData().toString() : "Unknown";
        Texture birdTexture;
        switch (birdType) {
            case "RedAngryBird":
                birdTexture = redBirdTexture;
                break;
            case "BlackAngryBird":
                birdTexture = blackBirdTexture;
                break;
            case "BlueAngryBird":
                birdTexture = blueBirdTexture;
                break;
            default:
                birdTexture = redBirdTexture;
        }
        batch.draw(birdTexture, birdBody.getPosition().x - 12, birdBody.getPosition().y - 12, 24, 24);

        for (WoodMaterial wood : woodBlocks) {
            Body woodBody = wood.getBody();
            Vector2 position = woodBody.getPosition();
            batch.draw(wood.getTexture(), position.x - 25, position.y - 10, 50, 20);
        }
        for (BasicPig pig : pig) {
            if (!pig.isMarkedForDestruction()) { //Only draw if not marked for destruction
                Vector2 position = pig.getBody().getPosition();
                batch.draw(pig.getTexture(), position.x - 45, position.y - 30, 35, 35); // Adjust size as needed
            }
        }

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);


        if (dragging) {
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.line(leftAnchor.x+40, leftAnchor.y-75, birdBody.getPosition().x, birdBody.getPosition().y);

            // Rope from right anchor to bird
            shapeRenderer.line(rightAnchor.x+34, rightAnchor.y-72, birdBody.getPosition().x, birdBody.getPosition().y);
            // shapeRenderer.line(catapultAnchor.x, catapultAnchor.y, birdBody.getPosition().x, birdBody.getPosition().y);
            drawTrajectory();
        }

        shapeRenderer.end();
    }




    private void drawTrajectory() {
        Vector2 startPosition = birdBody.getPosition();
        Vector2 velocity = catapultAnchor.cpy().sub(startPosition).scl(2f); // Predicted velocity

        // Predict the trajectory for 50 frames
        for (int i = 0; i < 50; i++) {
            float t = i / 10f; // Time interval
            float x = startPosition.x + velocity.x * t;
            float y = startPosition.y + velocity.y * t + 0.5f * world.getGravity().y * t * t;

            // Stop drawing if the trajectory goes below ground
            if (y < 0) break;

            shapeRenderer.circle(x, y, 2); // Draw a small circle
        }
    }


    private void destroyMarkedBodies() {
        List<WoodMaterial> toRemove = new ArrayList<>();
        for (WoodMaterial wood : woodBlocks) {
            if (wood.isMarkedForDestruction()) {
                world.destroyBody(wood.getBody());
                wood.dispose(); // Dispose of texture
                toRemove.add(wood);
            }
        }
        woodBlocks.removeAll(toRemove);

        List<BasicPig> toRemoves = new ArrayList<>();
        for (BasicPig pig : pig) {
            if (pig.isMarkedForDestruction()) {
                world.destroyBody(pig.getBody());
                pig.dispose(); // Dispose of texture
                toRemoves.add(pig);
            }
        }
        pig.removeAll(toRemoves);
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        // Dispose of textures and resources
        batch.dispose();
        shapeRenderer.dispose();
        redBirdTexture.dispose();
        blackBirdTexture.dispose();
        blueBirdTexture.dispose();
        background.dispose();
        catapult.dispose();
        world.dispose();
        for (WoodMaterial wood : woodBlocks) {
            wood.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }

    }

}
