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

public class GameScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // For drawing shapes like the rope and trajectory
    private Texture redBirdTexture;
    private Texture blackBirdTexture;
    private Texture blueBirdTexture;
    private Texture background;
    private Texture catapult;
    private List<Pig> pigs; // Store all pigs


    private World world; // Box2D world
    private Body birdBody; // Bird physics body
    private Body groundBody; // Ground physics body

    private boolean dragging = false; // To check if bird is being dragged
    private Vector2 initialPosition; // Bird's initial position
    private Vector2 launchVelocity = new Vector2(); // Initial velocity to be applied

    private Vector2 catapultAnchor; // Anchor point of the catapult

    private List<WoodMaterial> woodBlocks; // List to store wood blocks
    private List<GlassMaterial> glassBlocks; // List to store wood blocks

    private Queue<String> birdQueue; // Queue to manage birds in sequence
    private boolean isBirdActive = false;

    private Vector2 ropeEnd; // The dynamic endpoint of the rope during dragging
    private float maxStretch = 50f; // Maximum stretch distance
    private Vector2 leftAnchor; // Left end of the catapult
    private Vector2 rightAnchor; // Right end of the catapult



    public GameScreen(Game game) {
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
        birdQueue.add("BlackAngryBird");
        birdQueue.add("BlueAngryBird");

        // Spawn the first bird
        spawnNextBird();

        // Initialize wood blocks and create a level
        woodBlocks = new ArrayList<>();
        glassBlocks=new ArrayList<>();
        createLevel();

        setupCollisionListener();
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
                Body other = (a == birdBody) ? b : a;
                for (WoodMaterial wood : woodBlocks) {
                    if (wood.getBody() == other) {
                        wood.handleCollision(getBirdType(birdBody));
                        break;
                    }
                }
                for (GlassMaterial glass : glassBlocks) {
                    if (glass.getBody() == other) {
                        glass.handleCollision(getBirdType(birdBody));
                        break;
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



    private boolean birdInMotion() {
        if (birdBody == null) return false;

        // Check if the active bird has stopped moving or fallen below the ground
        boolean isResting = birdBody.getLinearVelocity().len2() < 0.01f && birdBody.getAngularVelocity() < 0.01f;
        boolean isBelowGround = birdBody.getPosition().y < 50; // Adjust for ground height

        return !(isResting || isBelowGround);
    }


    private void spawnNextBird() {
        if (birdQueue.isEmpty() || isBirdActive) return;

        Gdx.app.postRunnable(() -> {
            if (!isBirdActive && birdQueue.size() == birdQueue.size() - 1) {
                activateLevel(); // Activate blocks once the first bird is ready
            }

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
        // Dimensions for the wood blocks
        float blockWidth = 50;
        float blockHeight = 20;

        // Starting position for the vertical stack of wood blocks
        float startX = Gdx.graphics.getWidth() / 2 - blockWidth / 2 + 130; // Centered horizontally
        float startY = 100; // Distance from the bottom

        // Place 4 rectangular wood blocks vertically (left stack)
        int numVerticalBlocks = 4; // Number of blocks in the vertical stack
        for (int i = 0; i < numVerticalBlocks; i++) {
            float x = startX;
            float y = startY + i * blockHeight; // Stack blocks vertically
            woodBlocks.add(new WoodMaterial(world, x, y));
        }

        // Starting position for the horizontal layer of blocks
        float horizontalStartX = startX - blockWidth; // Start slightly to the left of the vertical column
        float horizontalStartY = startY + numVerticalBlocks * blockHeight; // Top of the vertical stack

        // Place 5 rectangular wood blocks horizontally
        int numHorizontalBlocks = 5; // Number of blocks in the horizontal layer
        for (int i = 0; i < numHorizontalBlocks; i++) {
            float x = horizontalStartX + i * blockWidth; // Position blocks side by side
            float y = horizontalStartY;
            if (i == 0) {
                continue; // Skip the first block
            }
            woodBlocks.add(new WoodMaterial(world, x, y));
        }

        // Starting position for the right vertical stack of blocks (below the last horizontal block)
        float rightVerticalStartX = horizontalStartX + (numHorizontalBlocks - 1) * blockWidth; // Align to the last block in the horizontal row
        float rightVerticalStartY = startY; // Same height as the left stack's bottom

        // Place 4 rectangular wood blocks vertically (right stack)
        for (int i = 0; i < numVerticalBlocks; i++) {
            float x = rightVerticalStartX;
            float y = rightVerticalStartY + i * blockHeight; // Stack blocks vertically
            woodBlocks.add(new WoodMaterial(world, x, y));
        }

        // Starting position for the triangular region on top
        float triangleBaseStartX = horizontalStartX+50; // Align with the horizontal layer
        float triangleBaseStartY = horizontalStartY + blockHeight; // Place directly above the horizontal layer

        // Create the triangular region
        int triangleRows = 3; // Number of rows in the triangle
        for (int row = 0; row < triangleRows; row++) {
            int blocksInRow = numHorizontalBlocks - 1 - row; // Number of blocks decreases as rows go up
            float rowStartX = triangleBaseStartX + (row * blockWidth) / 2; // Center the row
            float rowStartY = triangleBaseStartY + row * blockHeight; // Move up for each row

            for (int col = 0; col < blocksInRow; col++) {
                if(row==0 && (col==1 || col==2)){
                    continue;
                }
                if(row==1 && col==1){
                    continue;
                }
                float x = rowStartX + col * blockWidth; // Place blocks in a row
                float y = rowStartY; // Same height for the row
                glassBlocks.add(new GlassMaterial(world, x, y));
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

            // Reset rope position
            ropeEnd.set(catapultAnchor);
        }
    }

    private void activateLevel() {
        // Convert all wood blocks to DynamicBody
        for (WoodMaterial wood : woodBlocks) {
            wood.makeDynamic();
        }
        for (GlassMaterial glass : glassBlocks) {
            glass.makeDynamic();
        }
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

        // Handle input for dragging and launching
        handleInput();

        // Begin SpriteBatch to draw textures
        batch.begin();
        for (Pig pig : pigs) {
            pig.draw(batch);
        }

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(catapult, catapultAnchor.x, catapultAnchor.y - 75, 60, 40);

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
        for (GlassMaterial glass : glassBlocks) {
            Body glassBody = glass.getBody();
            Vector2 position = glassBody.getPosition();

            // Draw the glass block texture (adjust the coordinates for proper placement)
            batch.draw(glass.getTexture(), position.x - 25, position.y - 10, 50, 20);

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

        List<GlassMaterial> toRemoves = new ArrayList<>();
        for (GlassMaterial glass : glassBlocks) {
            if (glass.isMarkedForDestruction()) {
                world.destroyBody(glass.getBody());
                glass.dispose(); // Dispose of texture
                toRemoves.add(glass);
            }
        }
        glassBlocks.removeAll(toRemoves);
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
