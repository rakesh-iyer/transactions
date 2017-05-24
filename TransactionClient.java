class TransactionClient {
    public static void main(String args[]) {
        System.out.println("With Undo Log");
        doTransactions(new UndoLog(new FileLogImpl("UndologFile"), new MapDatabase()));
        System.out.println("With Redo Log");
        doTransactions(new RedoLog(new FileLogImpl("RedologFile"), new MapDatabase()));
    }

    static void doTransactions(Log log) {
        String tid = log.startTransaction();

        System.out.println("New Transaction");
        log.write(new Block(0), new Data("Zero"));
        log.write(new Block(1), new Data("One"));

        System.out.println("Before Transaction commited");
        log.dbdump();
        log.commitTransaction(tid);
        System.out.println("After Transaction commited");
        log.dbdump();
        System.out.println("New Transaction");

        tid = log.startTransaction();
        log.write(new Block(1), new Data("Three"));
        System.out.println("Before Transaction aborted");
        log.dbdump();

        log.abortTransaction(tid);
        System.out.println("After Transaction aborted");
        log.dbdump();
    }
}
