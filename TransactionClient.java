import java.util.*;
import java.util.concurrent.*;

class TransactionClient {
    final static int maxBlock = 8;
    static Database db = createDatabase(Database.LogType.UNDO);
    static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    static int threadInstanceNum;

    synchronized static int getThreadInstanceNum() {
        return threadInstanceNum++;
    }

    static int createTransactionThreads(int num) {
        int i = 0;
        try {
            for (; i < num; i++) {
                executorService.execute(new TransactionThread());
            }
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } finally {
            return i;
        }
    }

    static void shutdownTransactionThreads() throws InterruptedException {
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    public static void main(String args[]) throws Throwable {
        Thread mainThread = new Thread(new MainThread());

        createTransactionThreads(5);
        mainThread.start();
        mainThread.join();
        db.stopCheckpoint();
        db.dump();
        System.out.println("\n\nRecovering database\n\n");
        db.recover();
        db.dump();
    }

    static Database createDatabase(Database.LogType logType) {
        return new FileDatabase(logType);
    }

    static Integer[] getNonDuplicateRandoms(int count) {
        Random r = new Random();
        List<Integer> list = new ArrayList<>();

        while (list.size() < count) {
            Integer next = r.nextInt(maxBlock);
            if (!list.contains(next)) {
                list.add(next);
            }
        }
        return list.toArray(new Integer[0]);
    }


    static void doTransactions(int instanceNum) throws InterruptedException {
        /* Algorithm
            Get 3 random block numbers, read and update them with some data.
            Randomly abort a transaction.
        */
        while (true)  {
            String tid1 = db.startTransaction();
            String threadString = "Thread" + instanceNum + "_" + System.nanoTime();
            Integer[] list = getNonDuplicateRandoms(3);
            Arrays.sort(list);

            db.write(new Block(list[0]), new Data(threadString), tid1, true);
            db.write(new Block(list[1]), new Data(threadString), tid1, true);
            db.write(new Block(list[2]), new Data(threadString), tid1, true);
            db.commitTransaction(tid1);

            String tid2 = db.startTransaction();
            db.write(new Block(list[2]), new Data(threadString), tid2, true);
            db.abortTransaction(tid2);
        }
    }

    static class TransactionThread implements Runnable {
        int instanceNum;
        public void run() {
            try {
                instanceNum = getThreadInstanceNum();
                doTransactions(instanceNum);
            
            } catch (InterruptedException e) {
                System.out.println("Thread " + instanceNum + " done");
            }
        }
    }

    static class MainThread implements Runnable {
        public void run() {
            // wait a while.
            System.out.println("MainThread running");
            try {
                Thread.sleep(5000);
                // destroy all threads.
                System.out.println("Shutdown Threads");
                shutdownTransactionThreads();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
