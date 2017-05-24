interface Log {
    String startTransaction();    
    void commitTransaction(String tid);
    void abortTransaction(String tid);
    Data read(Block s);
    void write(Block s, Data d);
    void dump();
    void dbdump();
}

