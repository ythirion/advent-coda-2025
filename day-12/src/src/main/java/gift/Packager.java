package gift;

import gift.logging.Logger;
import gift.worker.Worker;

public class Packager {
    private final Logger logger;
    private final Worker worker;

    public Packager(Worker worker, Logger logger) {
        this.worker = worker;
        this.logger = logger;
    }

    public void packageAGift(Gift gift) {
        wrap(gift);
        addRibbon(gift);
    }

    private void wrap(Gift gift) {
        logger.log("Emballage du cadeau : " + gift.name());
        worker.run(3);
    }

    private void addRibbon(Gift gift) {
        logger.log("Ajout du ruban magique sur : " + gift.name());
        worker.run(2);
    }
}
