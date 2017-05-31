interface Database {
    enum LogType {
        REDO,
        UNDO
    }

    String startTransaction();
    Data read(Block b, String tid);
    void write(Block b, Data d, String tid);
    void commitTransaction(String tid);
    void abortTransaction(String tid);
    void dump();
}
