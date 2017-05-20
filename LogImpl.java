interface LogImpl {
    void writeRecord(LogRecord r);
    LogRecord readRecord();
    void deleteAllRecords();
}
