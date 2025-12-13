package gift;

import gift.delivery.Delivery;
import gift.delivery.DeliveryIssueException;
import gift.delivery.Randomizer;
import gift.logging.Console;
import gift.logging.Logger;
import gift.worker.ThreadWorker;

import static gift.GiftTypes.BOOK;
import static gift.GiftTypes.TEDDY;

public class Main {
    public static void main(String[] args) {
        var logger = new Console();
        var worker = new ThreadWorker();
        var randomizer = new Randomizer();

        GiftMachine machine = new GiftMachine(
                new GiftBuilder(logger),
                new Packager(worker, logger),
                new Delivery(worker, randomizer, logger),
                logger
        );

        createGiftFor(machine, TEDDY, "Alice", logger);
        createGiftFor(machine, BOOK, "Bob", logger);
    }

    private static void createGiftFor(GiftMachine machine,
                                      GiftTypes type,
                                      String recipient,
                                      Logger logger) {
        try {
            var gift = machine.createGift(type, recipient);
            logger.log("🎁 Résultat final : " + gift.name());
            logger.log("-----------------------------------");
        } catch (DeliveryIssueException e) {
            logger.error(e);
        }
    }
}
