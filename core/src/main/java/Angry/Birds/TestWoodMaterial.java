package Angry.Birds;

public class TestWoodMaterial {
    protected int durability; // Durability of the block
    private boolean markedForDestruction;

    public TestWoodMaterial() {
        // Initialize durability
        this.durability = 1;
        this.markedForDestruction = false;
    }

    // Method to simulate being hit by a bird
    public void takeHit() {
        durability -= 1;
        if (durability <= 0) {
            markedForDestruction = true;
            System.out.println("Destroyed");
        }
    }

    // Method to get the durability
    public void getDurability() {
        System.out.println("Durability: " + durability);
    }

    // Method to get the state of destruction
    public boolean isMarkedForDestruction() {
        return markedForDestruction;
    }
}
