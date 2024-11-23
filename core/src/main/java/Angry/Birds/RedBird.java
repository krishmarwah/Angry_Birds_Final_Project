package Angry.Birds;

class RedBird extends Bird {
    public RedBird(float x, float y) {
        super("red_bird.png", x, y);
    }

    @Override
    public void specialAbility() {
        // No special ability for Red Bird
        System.out.println("Red Bird has no special ability.");
    }
}

