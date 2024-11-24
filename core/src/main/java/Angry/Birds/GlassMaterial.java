package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GlassMaterial {
    private Body body;
    private Texture texture;
    private int durability; // Durability of the block
    private boolean markedForDestruction;
    private List<GlassMaterial> supportedBlocks = new ArrayList<>();

    public GlassMaterial(World world, float x, float y) {
        // Load texture
        this.texture = new Texture("glass.png");

        // Initialize durability
        this.durability = 3;

        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        // Create the body
        this.body = world.createBody(bodyDef);

        // Define shape and fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(25, 10); // Half-width and half-height

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // Lightweight for wood
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f; // Minimal bounce

        this.body.createFixture(fixtureDef);
        shape.dispose();

        this.markedForDestruction = false;
    }

    public void handleCollision(String birdType) {
        // Adjust durability based on the type of bird
        switch (birdType) {
            case "RedAngryBird":
                durability = 0; // Red bird instantly breaks the block
                break;

            case "BlackAngryBird":
                if (durability > 1) {
                    durability = 1; // Black bird reduces durability to 1
                } else {
                    durability = 0; // If already at 1, break the block
                }
                break;

            case "BlueAngryBird":
                durability -= 1; // Blue bird decreases durability by 1 per hit
                break;

            default:
                durability -= 1; // Default behavior for unknown birds
        }

        // If durability reaches 0, mark for destruction
        if (durability <= 0) {
            markedForDestruction = true;
        }
    }
    public void addSupportedBlock(GlassMaterial block) {
        supportedBlocks.add(block);
    }

    public void collapse() {
        for (GlassMaterial block : supportedBlocks) {
            block.takeHit(); // Each block above takes one hit
        }
    }

    public void makeDynamic() {
        // Convert the body to DynamicBody
        body.setType(BodyDef.BodyType.DynamicBody);
    }

    public void takeHit() {
        durability -= 1;
        if (durability <= 0) {
            markedForDestruction = true;
        }
    }


    public boolean isMarkedForDestruction() {

        return markedForDestruction;
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public void dispose() {
        texture.dispose();
    }
}
