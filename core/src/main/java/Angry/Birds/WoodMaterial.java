package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

public class WoodMaterial {
    private Body body;
    private Texture texture;
    private int durability; // Durability of the block
    private boolean markedForDestruction;

    public WoodMaterial(World world, float x, float y) {
        // Load texture
        this.texture = new Texture("wood.png");

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
        switch (birdType) {
            case "RedAngryBird":
                durability = 0; // Instant destruction
                break;
            case "BlueAngryBird":
                durability = Math.max(0, durability - 1); // Reduce durability by 1
                break;
            case "BlackAngryBird":
                durability = Math.max(0, durability - 1); // Reduce durability in stages
                if (durability > 0) durability -= 1;
                break;
            default:
                break;
        }

        // Mark for destruction if durability reaches 0
        if (durability == 0) {
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
