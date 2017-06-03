import java.util.*;
import java.util.concurrent.*;

class TransactionClient {
    final static int maxBlock = 8;
    static Database db = createDatabase(Database.LogType.UNDO);
    static ExecutorService executorService = new ForkJoinPool();

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

    static void joinTransactionThreads() {
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void shutdownTransactionThreads() {
        executorService.shutdownNow();
    }

    public static void main(String args[]) throws Throwable {
        Thread mainThread = new Thread(new MainThread());

        createTransactionThreads(5);
        mainThread.start();
        mainThread.join();
//        joinTransactionThreads();
        Thread.sleep(1000);
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


    static void doTransactions() {
        /* Algorithm
            Get 3 random block numbers, read and update them with some data.
            Randomly abort a transaction.
        */
        String tid1 = db.startTransaction();
        String threadString = Thread.currentThread().toString();
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

    static class TransactionThread implements Runnable {
        public void run() {
            doTransactions();
        }
    }

    static class MainThread implements Runnable {
        public void run() {
            // wait a while.
            System.out.println("MainThread running");
            try {
                Thread.sleep(0,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // destroy all threads.
//            shutdownTransactionThreads();
        }
    }
}
