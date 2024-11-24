package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class WoodMaterial {
    private Body body;
    private Texture texture;
    private int durability; // Durability of the block
    private boolean markedForDestruction;
    private List<WoodMaterial> supportedBlocks = new ArrayList<>(); // Blocks above this one

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

            markedForDestruction = true;

    }
    public void addSupportedBlock(WoodMaterial block) {
        supportedBlocks.add(block);
    }

    public void collapse() {
        for (WoodMaterial block : supportedBlocks) {
            block.takeHit(); // Each block above takes one hit
        }
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
