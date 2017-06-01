import java.util.*;

class TransactionClient {
    final static int maxBlock = 8;
    static Database db = createDatabase(Database.LogType.UNDO);

    public static void main(String args[]) {
        new Thread(new TransactionThread()).start(); 
        new Thread(new TransactionThread()).start(); 
        new Thread(new TransactionThread()).start(); 
        db.dump();
    }

    static Database createDatabase(Database.LogType logType) {
        return new FileDatabase(logType);
    }

    static void doTransactions() {
        /* Algorithm
            Get 3 random block numbers, read and update them with some data.
            Randomly abort a transaction.
        */
        Random r = new Random();
        String tid1 = db.startTransaction();
        String threadString = Thread.currentThread().toString();

        db.write(new Block(r.nextInt(maxBlock)), new Data(threadString), tid1, true);
        db.write(new Block(r.nextInt(maxBlock)), new Data(threadString), tid1, true);
        db.write(new Block(r.nextInt(maxBlock)), new Data(threadString), tid1, true);
        db.commitTransaction(tid1);

        String tid2 = db.startTransaction();
        db.write(new Block(r.nextInt(maxBlock)), new Data(threadString), tid2, true);
        db.abortTransaction(tid2);
    }

    static class TransactionThread implements Runnable {
        public void run() {
            doTransactions();
        }
    }
}
