public class DeadLockExample {

  private static final NamedLock LOCK_1 = new NamedLock("lock1");
  private static final NamedLock LOCK_2 = new NamedLock("lock2");
  private static final long PAUSE_MILLIS = 100;

  public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(lockingTask("Thread 1", LOCK_1, LOCK_2));
    Thread thread2 = new Thread(lockingTask("Thread 2", LOCK_2, LOCK_1));

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();
  }

  private static Runnable lockingTask(String threadName, NamedLock firstLock,
                                      NamedLock secondLock) {
    return () -> {
      synchronized (firstLock) {
        System.out.println(threadName + ": holds " + firstLock);
        try {
          Thread.sleep(PAUSE_MILLIS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
        System.out.println(threadName + ": waiting for " + secondLock);
        synchronized (secondLock) {
          System.out.println(threadName + ": holds " + firstLock + " and " +
                             secondLock);
        }
      }
    };
  }
}