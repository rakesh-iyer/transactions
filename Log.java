interface Log {
    String startTransaction();    
    commitTransaction(String tid);
    abortTransaction(String tid);
    writeRecord(Sector s, Data d);
}

