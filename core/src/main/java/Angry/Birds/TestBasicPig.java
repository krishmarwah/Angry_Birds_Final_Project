package Angry.Birds;

public class TestBasicPig {
    private int durability; // Durability of the pig
    private boolean markedForDestruction;
    private boolean disposed;

    public TestBasicPig() {
        // Initialize durability
        this.durability = 1;
        this.markedForDestruction = false;
        this.disposed = false;
    }

    // Simulate collision with a bird (Red, Black, Blue)
    public void handleCollision(String birdType) {
        switch (birdType) {
            case "RedAngryBird":
                durability -= 1;
                break;
            case "BlackAngryBird":
                durability -= 2;
                break;
            case "BlueAngryBird":
                durability -= 5;
                break;
            default:
                durability -= 1;
        }

        // Check if the pig should be destroyed
        if (durability <= 0) {
            markedForDestruction = true;
        }
    }

    // Method to simulate taking a hit
    public void takeHit() {
        durability -= 1;
        if (durability <= 0) {
            markedForDestruction = true;
        }
    }

    // Simulate disposing of the pig (marks it as disposed)
    public void dispose() {
        if (!disposed) {
            disposed = true;
        }
    }

    public int getDurability() {
        return durability;
    }

    public boolean isMarkedForDestruction() {
        return markedForDestruction;
    }

    public boolean isDisposed() {
        return disposed;
    }
}
