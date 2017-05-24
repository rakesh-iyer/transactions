class TransactionClient {
    public static void main(String args[]) {
        FileLogImpl logImpl = new FileLogImpl("logFile");
        MapDatabase db = new MapDatabase();

        UndoLog log = new UndoLog(logImpl, db);

        doTransactions(log, db);
    }

    static void doTransactions(Log log, Database db) {
        String tid = log.startTransaction();

        log.write(new Block(0), new Data("Zero"));

        log.write(new Block(1), new Data("One"));

        log.commitTransaction(tid);
        db.dump();
        System.out.println("Transaction commited");

        tid = log.startTransaction();
        log.write(new Block(1), new Data("Three"));
        db.dump();

        log.abortTransaction(tid);
        System.out.println("Transaction aborted");
        db.dump();
    }
}
