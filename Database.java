interface Database {
    enum LogType {
        REDO,
        UNDO
    }

    String startTransaction();
    Data read(Block b, String tid);
    void write(Block b, Data d, String tid, boolean append);
    void commitTransaction(String tid);
    void abortTransaction(String tid);
    void dump();
    void recover();
}
