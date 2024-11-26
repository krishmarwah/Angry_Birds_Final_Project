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

    public GlassMaterial(World world, float x, float y) {
        // Load texture
        this.texture = new Texture("glass.png");

        // Initialize durability
        this.durability = 2;

        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create the body
        this.body = world.createBody(bodyDef);

        // Define shape and fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(25, 10); // Half-width and half-height

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // Lightweight for wood
        fixtureDef.friction = 100f;
        fixtureDef.restitution = 0.2f;// Minimal bounce




        this.body=world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        this.markedForDestruction = false;
    }

    public void handleCollision(String birdType) {
        switch (birdType) {
            case "RedAngryBird":
                durability -= 1;
                break;
            case "BlackAngryBird":
                durability -= 2;
                break;
            case "BlueAngryBird":
                durability -= 4;
                break;
            default:
                durability -= 1; // Default durability reduction
        }

        // Check if the pig should be destroyed
        if (durability <= 0) {
            markedForDestruction = true;
        }
    }
    public void handleCollision() {

        markedForDestruction = true;

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

    public void makeDynamic() {
        // Convert the body to DynamicBody
        body.setType(BodyDef.BodyType.DynamicBody);
    }
    public void dispose() {
        texture.dispose();
    }
}
