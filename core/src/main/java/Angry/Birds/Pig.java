package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Base Pig Class
public abstract class Pig {
    protected Texture texture;
    protected float x, y;
    protected int health;  // Different pigs have different health

    public Pig(String textureFile, float x, float y, int health) {
        this.texture = new Texture(textureFile);
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println("Pig destroyed!");
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
