public class DeadLockExample {
  private static final Object lock1 = new Object();
  private static final Object lock2 = new Object();

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
      synchronized (lock1) {
        System.out.println("Thread 1: holds lock1");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        System.out.println("Thread 1: waiting for lock2");
        synchronized (lock2) {
          System.out.println("Thread 1: holds lock1 and lock2");
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      synchronized (lock2) {
        System.out.println("Thread 2: holds lock2");
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        System.out.println("Thread 2: waiting for lock1");
        synchronized (lock1) {
          System.out.println("Thread 2: holds lock2 and lock1");
        }
      }
    });

    thread1.start();
    thread2.start();
  }
}