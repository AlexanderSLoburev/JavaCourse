import java.util.concurrent.atomic.AtomicBoolean;

public class Worker {

  private static final long PAUSE_MILLIS = 100;

  private final String name;
  private final AtomicBoolean isActive = new AtomicBoolean(true);

  public Worker(String name) { this.name = name; }

  public void work(Worker otherWorker) {
    while (isActive.get()) {
      if (otherWorker.isActive.get()) {
        System.out.println(name + ": I give in - you go ahead and do it");
        try {
          Thread.sleep(PAUSE_MILLIS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
        continue;
      }
      System.out.println(name + ": I'm doing the work");
      isActive.set(false);
      otherWorker.isActive.set(true);
    }
  }
}
