public class AlternatingPrint {

  private static final Object LOCK = new Object();
  private static boolean turn = true;

  public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(printingTask("1", true));
    Thread thread2 = new Thread(printingTask("2", false));

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();
  }

  private static Runnable printingTask(String symbol, boolean isFirstThread) {
    return () -> {
      while (true) {
        synchronized (LOCK) {
          while (turn != isFirstThread) {
            try {
              LOCK.wait();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return;
            }
          }
          System.out.print(symbol + " ");
          turn = !isFirstThread;
          LOCK.notifyAll();
        }
      }
    };
  }
}