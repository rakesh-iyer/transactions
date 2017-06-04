interface Database {
    enum LogType {
        REDO,
        UNDO
    }

    String startTransaction();
    Data read(Block b, String tid) throws InterruptedException ;
    void write(Block b, Data d, String tid, boolean append) throws InterruptedException;
    void commitTransaction(String tid) throws InterruptedException;
    void abortTransaction(String tid) throws InterruptedException;;
    void recover() throws InterruptedException;;
    void dump();
}
