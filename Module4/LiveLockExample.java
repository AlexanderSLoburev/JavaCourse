public class LiveLockExample {

  public static void main(String[] args) throws InterruptedException {
    Worker worker1 = new Worker("Worker 1");
    Worker worker2 = new Worker("Worker 2");

    Thread thread1 = new Thread(workTask(worker1, worker2));
    Thread thread2 = new Thread(workTask(worker2, worker1));

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();
  }

  private static Runnable workTask(Worker worker, Worker otherWorker) {
    return () -> worker.work(otherWorker);
  }
}