package routing;

public record Gift(double weightKg, boolean fragile, String zone) {
    private static final double LOW_WEIGHT = 2.0;
    private static final double HEAVY_WEIGHT = 10.0;

    public boolean isValidZone() {
        return zone != null && !zone.isBlank();
    }

    public boolean isLowWeight() {
        return weightKg <= LOW_WEIGHT;
    }

    public boolean isHeavyWeight() {
        return weightKg > HEAVY_WEIGHT;
    }

    @Override
    public String toString() {
        return "Gift{w=" + weightKg + ", fragile=" + fragile + ", zone='" + zone + "'}";
    }
}