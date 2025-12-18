package routing;

import java.util.Optional;

public class GiftRouter {
    private static final String ERROR = "ERROR";
    private static final String WORKSHOP_HOLD = "WORKSHOP-HOLD";
    private static final String SLED = "SLED";
    private static final String REINDEER_EXPRESS = "REINDEER-EXPRESS";

    private static final String EUROPE = "EU";
    private static final String N_A = "NA";

    public String route(Gift gift) {
        return Optional.ofNullable(gift)
                .map(this::routeSafely)
                .orElse(ERROR);
    }

    private String routeSafely(Gift gift) {
        return gift.isValidZone()
                ? routeToZone(gift)
                : WORKSHOP_HOLD;
    }

    private String routeToZone(Gift gift) {
        return switch (gift) {
            case Gift g when g.fragile() -> routeFragileGift(g);
            case Gift g when g.isHeavyWeight() -> SLED;
            case Gift g when isForExpressZone(g) -> REINDEER_EXPRESS;
            default -> SLED;
        };
    }

    private static String routeFragileGift(Gift gift) {
        return gift.isLowWeight()
                ? REINDEER_EXPRESS
                : SLED;
    }

    private static boolean isForExpressZone(Gift gift) {
        return EUROPE.equals(gift.zone())
                || N_A.equals(gift.zone());
    }
}