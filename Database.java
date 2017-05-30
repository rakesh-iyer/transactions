interface Database {
    enum LogType {
        REDO,
        UNDO
    }

    String startTransaction();
    Data read(Block b);
    void write(Block b, Data d, String tid);
    void commitTransaction(String tid);
    void abortTransaction(String tid);
    void dump();
}
