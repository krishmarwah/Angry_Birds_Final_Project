package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.Serializable;

// Base Bird Class
public abstract class Bird implements Serializable {
    protected Texture texture;
    protected float x, y;  // Bird's position

    public Bird(String textureFile, float x, float y) {
        this.texture = new Texture(textureFile);
        this.x = x;
        this.y = y;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public abstract void specialAbility();  // Different birds can have different abilities

    public void dispose() {
        texture.dispose();
    }

    public void draw(SpriteBatch batch, float width, float height) {
        batch.draw(texture, x, y, width, height);  // Draw bird with specified width and height
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

}
