package Angry.Birds;

class BlueBird extends Bird {
    public BlueBird(float x, float y) {
        super("blue_bird.png", x, y);
    }

    @Override
    public void specialAbility() {
        BlueBird.split();
    }

    // Extracted static method for testing
    public static void split() {
        System.out.println("Splits into Three.");
    }
}
