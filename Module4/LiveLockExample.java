import java.util.concurrent.atomic.AtomicBoolean;

public class LiveLockExample {
  static class Worker {
    private final String name;
    private final AtomicBoolean isActive = new AtomicBoolean(true);

    Worker(String name) { this.name = name; }

    public void work(Worker other) {
      while (isActive.get()) {
        if (other.isActive.get()) {
          System.out.println(name + ": I give in - you go ahead and do it");
          try { Thread.sleep(100); } catch (InterruptedException e) {}
          continue;
        }
        System.out.println(name + ": I'm doing the work");
        isActive.set(false);
        other.isActive.set(true);
      }
    }
  }

  public static void main(String[] args) {
    Worker worker1 = new Worker("Worker 1");
    Worker worker2 = new Worker("Worker 2");

    new Thread(() -> worker1.work(worker2)).start();
    new Thread(() -> worker2.work(worker1)).start();
  }
}