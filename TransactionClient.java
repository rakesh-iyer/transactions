import java.util.*;

class TransactionClient {
    final static int maxBlock = 8;
    static Database db = createDatabase(Database.LogType.UNDO);
    static Thread[] threads = createThreads(5);

    static Thread[] createThreads(int threads) {
        Thread[] array = new Thread[threads];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Thread(new TransactionThread());
        }

        return array;
    }

    static void startThreads(Thread[] threads) {
        for (Thread t : threads) {
            t.start();
        }
    }

    static void joinThreads(Thread[] threads) throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }

    static void destroyThreads(Thread[] threads) throws InterruptedException {
        for (Thread t : threads) {
            t.suspend();
        }
    }

    public static void main(String args[]) throws Throwable {
        Thread mainThread = new Thread(new MainThread());

        mainThread.start();
        startThreads(threads);
        joinThreads(threads);
        mainThread.join();

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
//                Thread.sleep(0,1);
                // destroy all threads.
                destroyThreads(threads);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
