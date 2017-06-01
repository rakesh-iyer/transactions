interface Datastore {
    Data read(Block s);
    void write(Block s, Data data, Integer lsn);
    void dump();
}
    
