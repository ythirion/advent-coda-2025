package routing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

class GiftRouterTest {
    private final GiftRouter router = new GiftRouter();

    @Test
    @DisplayName("Null gift -> ERROR")
    void error_for_an_existing_gift() {
        assertEquals("ERROR", router.route(null));
    }

    @ParameterizedTest(name = "[{index}] zone=\"{0}\" -> WORKSHOP-HOLD")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    @DisplayName("Empty/blank zone -> WORKSHOP-HOLD")
    void hold_for_blank_zone(String zone) {
        var gift = new Gift(1.0, false, zone);
        assertEquals("WORKSHOP-HOLD", router.route(gift));
    }

    @ParameterizedTest(name = "[{index}] w={0}kg, fragile={1}, zone={2} -> {3}")
    @CsvSource({
            "2.0,  true,  EU,   REINDEER-EXPRESS",
            "2.1,  true,  EU,   SLED",
            "10.1, false, EU,   SLED",
            "9.9,  false, EU,   REINDEER-EXPRESS",
            "9.9,  false, NA,   REINDEER-EXPRESS",
            "9.9,  false, APAC, SLED"
    })
    @DisplayName("Routing matrix (no null/blank zone)")
    void routingMatrix(double weightKg, boolean fragile, String zone, String expectedRoute) {
        var gift = new Gift(weightKg, fragile, zone);
        assertEquals(expectedRoute, router.route(gift));
    }
}