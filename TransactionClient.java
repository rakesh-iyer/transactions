class TransactionClient {
    public static void main(String args[]) {
        doTransactions(new FileDatabase(Database.LogType.UNDO));
        doTransactions(new FileDatabase(Database.LogType.REDO));
    }

    static void doTransactions(Database db) {
        String tid = db.startTransaction();

        System.out.println("New Transaction");
        db.write(new Block(0), new Data("Zero"));
        db.write(new Block(1), new Data("One"));

        System.out.println("Before Transaction commited");
        db.dump();
        db.commitTransaction(tid);
        System.out.println("After Transaction commited");
        db.dump();
        System.out.println("New Transaction");

        tid = db.startTransaction();
        db.write(new Block(1), new Data("Three"));
        System.out.println("Before Transaction aborted");
        db.dump();

        db.abortTransaction(tid);
        System.out.println("After Transaction aborted");
        db.dump();
    }
}
