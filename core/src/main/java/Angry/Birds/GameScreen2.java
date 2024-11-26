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


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameScreen2 implements Screen {
    private int score=0;
    private Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // For drawing shapes like the rope and trajectory
    private Texture redBirdTexture;
    private Texture blackBirdTexture;
    private Texture blueBirdTexture;
    private Texture background;
    private Texture catapult;
    private Texture pauseButton;

    private boolean isPaused = false;


    private List<Pig> pigs;// Store all pigs
    private List<GlassMaterial> glassBlocks = new ArrayList<>();
    private List<StoneMaterial> stoneBlocks = new ArrayList<>();
    private List<KingPig> kingPigs;

    private World world; // Box2D world
    private Body birdBody; // Bird physics body
    private Body groundBody; // Ground physics body

    private boolean dragging = false; // To check if bird is being dragged
    private Vector2 initialPosition; // Bird's initial position
    private Vector2 launchVelocity = new Vector2(); // Initial velocity to be applied

    private Vector2 catapultAnchor; // Anchor point of the catapult

    private List<WoodMaterial> woodBlocks; // List to store wood blocks
    private List<GlassMaterial> glassblocks; // List to store wood blocks

    private Queue<String> birdQueue; // Queue to manage birds in sequence
    private boolean isBirdActive = false;
    private boolean birdTouchedGround = false; // To check if the bird touched the ground

    private Vector2 ropeEnd; // The dynamic endpoint of the rope during dragging
    private float maxStretch = 50f; // Maximum stretch distance
    private Vector2 leftAnchor; // Left end of the catapult
    private Vector2 rightAnchor; // Right end of the catapult



    public GameScreen2(Game game) {
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
        birdQueue.add("BlackAngryBird");
        birdQueue.add("BlackAngryBird");
        birdQueue.add("BlueAngryBird");

        // Spawn the first bird
        spawnNextBird();

        // Initialize wood blocks and create a level
        woodBlocks = new ArrayList<>();
        stoneBlocks=new ArrayList<>();
        kingPigs=new ArrayList<>();
        createLevel();

        setupCollisionListener();
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
        groundDef.position.set(0, 82); // Ground at y = 0
        groundDef.type = BodyDef.BodyType.StaticBody;

        groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(Gdx.graphics.getWidth(), 10); // Ground width and thickness

        groundBody.createFixture(groundShape, 0.0f);
        groundShape.dispose();
    }

    private void setupCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Body a = fixtureA.getBody();
                Body b = fixtureB.getBody();

                if ((a == birdBody || b == birdBody) &&
                    (a == groundBody || b == groundBody)) {

                    // If the active bird contacts the ground
                    isBirdActive = false;
                    spawnNextBird(); // Spawn the next bird
                }

                // Handle other collisions (e.g., with wood blocks)
                // Handle other collisions (e.g., with wood blocks)
                if(a.equals(birdBody) || b.equals(birdBody)) {
                    Body other = (a == birdBody) ? b : a;
                    for (WoodMaterial wood : woodBlocks) {
                        if (wood.getBody() == other) {
                            wood.handleCollision(getBirdType(birdBody));
                            updateScore(50); // Award points for hitting a pig

                            break;
                        }
                    }
                    for (GlassMaterial glass : glassBlocks) {
                        if (glass.getBody() == other) {
                            glass.handleCollision(getBirdType(birdBody));
                            updateScore(50); // Award points for hitting a pig

                            break;
                        }
                    }
                    for (StoneMaterial stone : stoneBlocks) {
                        if (stone.getBody() == other) {
                            stone.handleCollision(getBirdType(birdBody));
                            updateScore(50); // Award points for hitting a pig

                            break;
                        }
                    }


                }
                for (KingPig pig : kingPigs) {
                    Body pigBody = pig.getBody();
                    if(a!=birdBody && b!=birdBody){
                        continue;
                    }

                    // Bird-pig collision
                    if ((a == birdBody && b == pigBody) ) {
                        String birdType = a.getUserData() != null ? birdBody.getUserData().toString() : "Unknown";
                        System.out.println(birdType);
                        pig.handleCollision(birdType);
                        if(pig.isMarkedForDestruction()) {
                            updateScore(500);
                        }; // Award points for hitting a pig
                    }else if(b == birdBody && a == pigBody){
                        String birdType = b.getUserData() != null ? birdBody.getUserData().toString() : "Unknown";
                        System.out.println(birdType);
                        pig.handleCollision(birdType);
                        if(pig.isMarkedForDestruction()) {
                            updateScore(500);
                        }
                    }

                    // Ground-pig collision
                    if ((a == groundBody && b == pigBody) ||
                        (b == groundBody && a == pigBody)) {
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
        if (score >= 700) {
            goToWinPage(); // Transition to win screen if score >= 500
        }
    }
    private void goToWinPage() {
        // Logic to transition to the win page
        game.setScreen(new WinPage(game,2)); // Assuming WinPageScreen is implemented
    }
    private void checkGameEnd() {
        if (score < 500) {
            goToLosePage(); // Transition to the lose page
        }
    }
    private void goToLosePage() {
        game.setScreen(new LosePage(game,2)); // Assuming LosePage class is implemented
    }



    private boolean birdInMotion() {
        if (birdBody == null) return false;

        // Check if the active bird has stopped moving or fallen below the ground
        boolean isResting = birdBody.getLinearVelocity().len2() < 0.01f && birdBody.getAngularVelocity() < 0.01f;
        boolean isBelowGround = birdBody.getPosition().y < 50; // Adjust for ground height

        return !(isResting || isBelowGround);

    }


    private void spawnNextBird() {


        if (birdQueue.isEmpty() ) {
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

    private void createLevel() {
        // Dimensions for the blocks
        float blockWidth = 50;
        float blockHeight = 20;

        // Starting position for the inverted triangle of wooden blocks
        float baseX = Gdx.graphics.getWidth() / 2 - blockWidth * 1.5f; // Center horizontally
        float baseY = 75; // Base position for the wooden material

        // Number of rows for the inverted triangle
        int triangleRows = 3;

        // Create the inverted triangle of WoodenMaterial blocks
        for (int row = 0; row < triangleRows; row++) {
            // Calculate the number of blocks in the current row
            int blocksInRow = row + 1;

            // Calculate the starting X position for this row
            float rowStartX = baseX - (row * blockWidth) / 2;

            // Calculate the Y position for this row
            float rowY = baseY + row * blockHeight;

            for (int col = 0; col < blocksInRow; col++) {
                if(row==2 && col==1){
                    continue;
                }
                float x = rowStartX + col * blockWidth+250; // Position blocks side by side
                float y = rowY+30;

                // Add the WoodenMaterial block
                woodBlocks.add(new WoodMaterial(world, x, y));
            }
        }

        // Starting position for the GlassMaterial blocks (on top of the wooden triangle)
        float glassBaseY = baseY + triangleRows * blockHeight; // Place above the wooden triangle

        // Create the triangle of GlassMaterial blocks
        for (int row = 0; row < triangleRows; row++) {
            // Calculate the number of blocks in the current row
            int blocksInRow = triangleRows - row;

            // Calculate the starting X position for this row
            float rowStartX = baseX + (row * (blockWidth+5)) / 2;

            // Calculate the Y position for this row
            float rowY = glassBaseY + row * (blockHeight+5);

            for (int col = 0; col < blocksInRow; col++) {
                if (row == 0 && col == 1) {
                    continue; // Skip the center block of the top row
                }
                float x = rowStartX + col * (blockWidth+5)+200; // Position blocks side by side
                float y = rowY+30;

                // Add the GlassMaterial block
                glassBlocks.add(new GlassMaterial(world, x, y));
            }
        }
        // Extend the GlassMaterial blocks diagonally to form complete triangles
        for (int row = 0; row < 2; row++) {
            float x = baseX - (row + 1) * blockWidth / 2 + 373; // Adjust left diagonal X position
            float y = baseY + (triangleRows + row) * blockHeight -30; // Adjust Y position
            stoneBlocks.add(new StoneMaterial(world, x, y));
        }

        // Right diagonal of GlassMaterial
        for (int row = 0; row < 2; row++) {
            float x = baseX + (triangleRows + row) * blockWidth / 2 + 75; // Adjust right diagonal X position
            float y = baseY + (triangleRows + row) * blockHeight -30; // Adjust Y position
            stoneBlocks.add(new StoneMaterial(world, x, y));
        }
        float kingPigX = baseX + blockWidth * ((triangleRows)/ 2f) ; //Center horizontally
        float kingPigY = baseY + (triangleRows * blockHeight);//Center vertically
        System.out.println(kingPigY);
        kingPigs.add(new KingPig(world, kingPigX+162, kingPigY-9.8f));//Add to kingPigs list

    }


    @Override
    public void render(float delta) {
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
                game.setScreen(new PauseScreen(game,2,null,null,this));
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

        // Draw the glass blocks
        for (GlassMaterial glass : glassBlocks) {
            Body glassBody = glass.getBody();
            Vector2 position = glassBody.getPosition();

            // Draw the glass block texture (adjust the coordinates for proper placement)
            batch.draw(glass.getTexture(), position.x - 25, position.y - 10, 50, 20);

        }
        for (StoneMaterial stone : stoneBlocks) {
            Body glassBody = stone.getBody();
            Vector2 position = glassBody.getPosition();

            // Draw the glass block texture (adjust the coordinates for proper placement)
            batch.draw(stone.getTexture(), position.x - 25, position.y - 10, 50, 20);

        }

        // Draw wood blocks (if applicable)
        for (WoodMaterial wood : woodBlocks) {
            Body woodBody = wood.getBody();
            Vector2 position = woodBody.getPosition();
            batch.draw(wood.getTexture(), position.x - 25, position.y - 10, 50, 20);
        }

        for (KingPig pig : kingPigs) {
            if (!pig.isMarkedForDestruction()) { //Only draw if not marked for destruction
                Vector2 position = pig.getBody().getPosition();
                batch.draw(pig.getTexture(), position.x, position.y , 35, 35); // Adjust size as needed
            }
        }

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if (dragging) {
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.line(leftAnchor.x+40, leftAnchor.y-75, birdBody.getPosition().x, birdBody.getPosition().y);

            // Rope from right anchor to bird
            shapeRenderer.line(rightAnchor.x+34, rightAnchor.y-72, birdBody.getPosition().x, birdBody.getPosition().y);
            drawTrajectory();
        }

        shapeRenderer.end();
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
            for (KingPig pig : kingPigs) {
                if (!pig.isMarkedForDestruction()) { //Only draw if not marked for destruction
                    pig.settotrue();
                }
            }

            // Reset rope position
            ropeEnd.set(catapultAnchor);
        }
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

        List<GlassMaterial> toRemoves = new ArrayList<>();
        for (GlassMaterial glass : glassBlocks) {
            if (glass.isMarkedForDestruction()) {
                world.destroyBody(glass.getBody());
                glass.dispose(); // Dispose of texture
                toRemoves.add(glass);
            }
        }
        glassBlocks.removeAll(toRemoves);

        List<StoneMaterial> toRemovess = new ArrayList<>();
        for (StoneMaterial stone : stoneBlocks) {
            if (stone.isMarkedForDestruction()) {
                world.destroyBody(stone.getBody());
                stone.dispose(); // Dispose of texture
                toRemovess.add(stone);
            }
        }
        stoneBlocks.removeAll(toRemovess);

        List<KingPig> toRemov = new ArrayList<>();
        for (KingPig pig : kingPigs) {
            if (pig.isMarkedForDestruction()) {
                world.destroyBody(pig.getBody());
                pig.dispose(); // Dispose of texture
                toRemov.add(pig);
            }
        }
        kingPigs.removeAll(toRemov);
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
