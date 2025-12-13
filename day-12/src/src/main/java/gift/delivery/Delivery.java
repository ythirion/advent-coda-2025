package gift.delivery;

import gift.logging.Logger;
import gift.worker.Worker;

public class Delivery {
    private final Worker worker;
    private final Randomizer randomizer;
    private final Logger logger;

    public Delivery(Worker worker,
                    Randomizer randomizer,
                    Logger logger) {
        this.worker = worker;
        this.randomizer = randomizer;
        this.logger = logger;
    }

    public void deliver(String recipient) throws DeliveryIssueException {
        logger.log("Livraison en cours vers l'atelier de distribution...");
        worker.run(4);

        if (randomizer.randomInt() > 8) {
            throw new DeliveryIssueException("Erreur de livraison : le traîneau est tombé en panne.");
        }
        logger.log("Cadeau livré à la zone d’expédition pour " + recipient);
    }
}
