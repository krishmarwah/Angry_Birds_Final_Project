package Angry.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Base Material Class
public abstract class Material {
    protected Texture texture;
    protected float x, y;
    protected int durability;  // Materials have durability

    public Material(String textureFile, float x, float y, int durability) {
        this.texture = new Texture(textureFile);
        this.x = x;
        this.y = y;
        this.durability = durability;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void takeDamage(int damage) {
        durability -= damage;
        if (durability <= 0) {
            System.out.println("Material destroyed!");
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        texture.dispose();
    }
}
