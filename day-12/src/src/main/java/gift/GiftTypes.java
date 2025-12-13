package gift;

public enum GiftTypes {
    TEDDY("🧸 Ourson en peluche pour %s"),
    CAR("🚗 Petite voiture pour %s"),
    DOLL("🪆 Poupée magique pour %s"),
    BOOK("📚 Livre enchanté pour %s");

    private final String messageFormat;

    GiftTypes(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public String messageFor(String recipient) {
        return String.format(messageFormat, recipient);
    }
}