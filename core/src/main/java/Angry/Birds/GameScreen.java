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

    private World world; // Box2D world
    private Body birdBody; // Bird physics body
    private Body groundBody; // Ground physics body

    private boolean dragging = false; // To check if bird is being dragged
    private Vector2 initialPosition; // Bird's initial position
    private Vector2 launchVelocity = new Vector2(); // Initial velocity to be applied

    private Vector2 catapultAnchor; // Anchor point of the catapult

    private List<WoodMaterial> woodBlocks; // List to store wood blocks
    private Queue<String> birdQueue; // Queue to manage birds in sequence
    private boolean isBirdActive = false;


    public GameScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        // Load textures
        redBirdTexture = new Texture("red_bird.png");
        blackBirdTexture = new Texture("black_bird.png");
        blueBirdTexture = new Texture("blue_bird.png");
        background = new Texture("bg.png");
        catapult = new Texture("catapult.png");

        // Initialize Box2D world with gravity
        world = new World(new Vector2(0, -9.8f), true);

        // Create the ground body
        createGround();

        // Initialize the catapult anchor position
        catapultAnchor = new Vector2(100, 200); // Fixed position for the catapult

        // Initialize bird queue
        birdQueue = new LinkedList<>();
        birdQueue.add("RedAngryBird");
        birdQueue.add("BlackAngryBird");
        birdQueue.add("BlueAngryBird");

        // Spawn the first bird
        spawnNextBird();

        // Initialize wood blocks and create a level
        woodBlocks = new ArrayList<>();
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
                String birdType = getBirdType(birdBody);

                if (a == birdBody || b == birdBody) {
                    Body other = (a == birdBody) ? b : a;

                    // Check if the other body is a wood block
                    for (WoodMaterial wood : woodBlocks) {
                        if (wood.getBody() == other) {
                            // Mark wood for destruction
                            wood.handleCollision(birdType);
                            break;
                        }
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

        // Check if the bird has come to rest or fallen below the ground
        boolean isResting = birdBody.getLinearVelocity().len2() < 0.01f && birdBody.getAngularVelocity() < 0.01f;
        boolean isBelowGround = birdBody.getPosition().y < 50; // Adjust as per ground height

        return !(isResting || isBelowGround);
    }

    private void spawnNextBird() {
        if (birdQueue.isEmpty() || isBirdActive) return;

        Gdx.app.postRunnable(() -> {
            String birdType = birdQueue.poll();
            createBird(birdType);
            initialPosition = birdBody.getPosition().cpy();
            isBirdActive = true; // Bird is now active
        });
    }


    private void createBird(String birdType) {
        BodyDef birdDef = new BodyDef();
        birdDef.type = BodyDef.BodyType.DynamicBody;
        birdDef.position.set(catapultAnchor.x, catapultAnchor.y); // Initial position at the catapult

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
    }

    private void createLevel() {
        // Dimensions for the wood blocks
        float blockWidth = 50;
        float blockHeight = 20;

        // Positioning constants
        float startX = Gdx.graphics.getWidth() - 300; // Start near the right side of the screen
        float startY = 100; // Distance from the bottom

        // Base layer of wood blocks (horizontal row)
        for (int i = 0; i < 3; i++) {
            woodBlocks.add(new WoodMaterial(world, startX + i * blockWidth, startY));
        }

        // Second layer of wood blocks (slightly above the base layer)
        woodBlocks.add(new WoodMaterial(world, startX + blockWidth / 2, startY + blockHeight));
        woodBlocks.add(new WoodMaterial(world, startX + blockWidth * 2.5f, startY + blockHeight));

        // Top layer of wood block (centered above the second layer)
        woodBlocks.add(new WoodMaterial(world, startX + blockWidth * 1.5f, startY + blockHeight * 2));
    }

    private void handleInput() {
        if (!isBirdActive) return; // Ignore input if no active bird

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (!dragging && touchPoint.dst(birdBody.getPosition()) < 20) {
                dragging = true;
            }

            if (dragging) {
                Vector2 adjustedPosition = touchPoint.cpy();
                float maxDistance = 50f;
                if (adjustedPosition.dst(catapultAnchor) > maxDistance) {
                    adjustedPosition = catapultAnchor.cpy().add(
                        adjustedPosition.sub(catapultAnchor).nor().scl(maxDistance)
                    );
                }
                birdBody.setTransform(adjustedPosition, birdBody.getAngle());
            }
        } else if (dragging) {
            dragging = false;

            Vector2 releasePosition = birdBody.getPosition();
            launchVelocity.set(catapultAnchor.cpy().sub(releasePosition).scl(1.5f));
            birdBody.setLinearVelocity(launchVelocity);

            // Allow the bird to move

    // Spawn the next bird after a delay (simulate bird switching)
            Gdx.app.postRunnable(this::spawnNextBird);
        }
    }

    @Override
    public void render(float delta) {
        // Update the Box2D world
        world.step(1 / 60f, 6, 2);

        // Check if the bird is inactive
        if (isBirdActive && !birdInMotion()) {
            isBirdActive = false; // Mark the bird as inactive
            spawnNextBird(); // Spawn the next bird
        }

        // Clear the screen
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input for dragging and launching
        handleInput();

        // Begin SpriteBatch to draw textures
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(catapult, catapultAnchor.x - 30, catapultAnchor.y - 30, 60, 60);

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

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if (dragging) {
            shapeRenderer.setColor(0, 0, 0, 1);
            shapeRenderer.line(catapultAnchor.x, catapultAnchor.y, birdBody.getPosition().x, birdBody.getPosition().y);
            drawTrajectory();
        }

        shapeRenderer.end();
    }


    private void drawTrajectory() {
        Vector2 startPosition = birdBody.getPosition();
        Vector2 velocity = catapultAnchor.cpy().sub(startPosition).scl(1.5f); // Predicted velocity

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
    }
}

