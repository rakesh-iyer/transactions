interface Database {
    enum LogType {
        REDO,
        UNDO
    }

    Data read(Block b);
    void write(Block b, Data d);
    String startTransaction();
    void commitTransaction(String tid);
    void abortTransaction(String tid);
    void dump();
}
