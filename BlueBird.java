package Angry.Birds;

class BlueBird extends Bird {
    public BlueBird(float x, float y) {
        super("blue_bird.png", x, y);
    }

    @Override
    public void specialAbility() {
        // Speed boost ability
        System.out.println("Yellow Bird uses speed boost!");
    }
}
