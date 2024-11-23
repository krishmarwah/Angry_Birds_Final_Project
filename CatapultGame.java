package Angry.Birds;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class CatapultGame extends Game {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body catapultArm;
    private Body rock;
    private Body catapultBase;
    private SpriteBatch batch;
    private Texture rockTexture;

    @Override
    public void create() {
        // Initialize world
        world = new World(new Vector2(0, -10), true);  // Gravity pointing downward
        debugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        // Initialize textures
        //rockTexture = new Texture(Gdx.files.internal("rock.png"));

        // Create catapult base (static body)
        createCatapultBase();

        // Create catapult arm (dynamic body)
        createCatapultArm();

        // Create rock (dynamic body)
        createRock();
    }

    private void createCatapultBase() {
        // Create a static body for the catapult base
        BodyDef baseDef = new BodyDef();
        baseDef.type = BodyDef.BodyType.StaticBody;
        baseDef.position.set(10, 5);
        catapultBase = world.createBody(baseDef);

        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(2, 0.5f);  // Rectangle base

        FixtureDef baseFixture = new FixtureDef();
        baseFixture.shape = baseShape;
        baseFixture.density = 0.0f;  // Static body, no density
        catapultBase.createFixture(baseFixture);
    }

    private void createCatapultArm() {
        // Create a dynamic body for the catapult arm
        BodyDef armDef = new BodyDef();
        armDef.type = BodyDef.BodyType.DynamicBody;
        armDef.position.set(10, 5);
        catapultArm = world.createBody(armDef);

        PolygonShape armShape = new PolygonShape();
        armShape.setAsBox(4, 0.2f);  // A long, horizontal rectangle for the arm

        FixtureDef armFixture = new FixtureDef();
        armFixture.shape = armShape;
        armFixture.density = 1.0f;  // Density for physical interactions
        armFixture.restitution = 0.5f;  // Bounciness

        catapultArm.createFixture(armFixture);

        // Attach the arm to the base with a revolute joint (rotation point)
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = catapultBase;
        jointDef.bodyB = catapultArm;
        jointDef.localAnchorA.set(0, 0);
        jointDef.localAnchorB.set(-4, 0);
        jointDef.enableMotor = true;
        jointDef.motorSpeed = 0.0f;  // No initial speed
        jointDef.maxMotorTorque = 100f;
        world.createJoint(jointDef);
    }

    private void createRock() {
        // Create a dynamic body for the rock
        BodyDef rockDef = new BodyDef();
        rockDef.type = BodyDef.BodyType.DynamicBody;
        rockDef.position.set(6, 6);
        rock = world.createBody(rockDef);

        CircleShape rockShape = new CircleShape();
        rockShape.setRadius(0.5f);  // Rock is circular

        FixtureDef rockFixture = new FixtureDef();
        rockFixture.shape = rockShape;
        rockFixture.density = 1.0f;
        rockFixture.restitution = 0.7f;  // Bouncy rock

        rock.createFixture(rockFixture);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the world step (simulate physics)
        world.step(1 / 60f, 6, 2);

        // Render the debug view
        debugRenderer.render(world, batch.getProjectionMatrix().cpy().scale(1, -1, 1));

        batch.begin();
        // Draw the rock
        batch.draw(rockTexture, rock.getPosition().x - 0.5f, rock.getPosition().y - 0.5f, 1, 1);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Object camera = null;
//        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
        rockTexture.dispose();
    }
}

