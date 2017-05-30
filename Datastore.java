interface Datastore {
    Data read(Block s);
    void write(Block s, Data data, String lsn);
    void dump();
}
    
