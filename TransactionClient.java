import java.util.*;

class TransactionClient {
    final static int maxBlock = 8;
    static Database db = createDatabase(Database.LogType.UNDO);

    public static void main(String args[]) throws Throwable {
        Thread t1 = new Thread(new TransactionThread());
        Thread t2 = new Thread(new TransactionThread());
        Thread t3 = new Thread(new TransactionThread());

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
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
}
