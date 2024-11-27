package Angry.Birds;

class BlackBird extends Bird {
    public BlackBird(float x, float y) {
        super("black_bird.png", x, y);
    }

    @Override
    public void specialAbility() {
        BlackBird.explode();
    }

    // Extracted static method
    public static void explode() {
        System.out.println("Black Bird explodes on impact!");
    }
}
