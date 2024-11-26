package Angry.Birds;

class BlackBird extends Bird {
    public BlackBird(float x, float y) {
        super("black_bird.png", x, y);
    }

    @Override
    public void specialAbility() {
        // Explodes on impact
        System.out.println("Black Bird explodes on impact!");
    }
}





















