package gift;

import gift.logging.Logger;

public class GiftBuilder {
    private final Logger logger;

    public GiftBuilder(Logger logger) {
        this.logger = logger;
    }

    public Gift build(GiftTypes type, String recipient) {
        logger.log("Construction du cadeau de type '" + type + "'...");
        return new Gift(type.messageFor(recipient));
    }
}
