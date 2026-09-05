public class AlternatingPrint {
  private static final Object lock = new Object();
  private static boolean turn = true;

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
      while (true) {
        synchronized (lock) {
          while (!turn) {
            try { lock.wait(); } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return;
            }
          }
          System.out.print("1 ");
          turn = false;
          lock.notifyAll();
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      while (true) {
        synchronized (lock) {
          while (turn) {
            try { lock.wait(); } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return;
            }
          }
          System.out.print("2 ");
          turn = true;
          lock.notifyAll();
        }
      }
    });

    thread1.start();
    thread2.start();
  }
}