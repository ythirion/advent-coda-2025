package gift;

import gift.delivery.Delivery;
import gift.delivery.DeliveryIssueException;
import gift.logging.Logger;

public class GiftMachine {
    private final GiftBuilder builder;
    private final Packager packager;
    private final Delivery delivery;
    private final Logger logger;

    public GiftMachine(GiftBuilder builder,
                       Packager packager,
                       Delivery delivery,
                       Logger logger) {
        this.builder = builder;
        this.packager = packager;
        this.delivery = delivery;
        this.logger = logger;
    }

    public Gift createGift(GiftTypes type, String recipient) throws DeliveryIssueException {
        logger.log("Démarrage de la création du cadeau pour " + recipient);

        var gift = builder.build(type, recipient);
        packager.packageAGift(gift);
        delivery.deliver(recipient);

        logger.log("Cadeau prêt pour " + recipient + " : " + gift.name());

        return gift;
    }
}
