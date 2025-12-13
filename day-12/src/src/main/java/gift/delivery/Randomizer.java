package gift.delivery;

import java.util.Random;

public class Randomizer {
    private final Random random = new Random();

    public int randomInt() {
        return random.nextInt(11);
    }
}
