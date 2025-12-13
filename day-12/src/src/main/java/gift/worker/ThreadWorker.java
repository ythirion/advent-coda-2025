package gift.worker;

public class ThreadWorker implements Worker {
    @Override
    public void run(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
