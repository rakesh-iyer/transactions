import java.util.*;

interface LogImpl {
    int getFirstLSN(String tid);
    void writeRecord(LogRecord r);
    List<LogRecord> readAllRecords();
    List<LogRecord> readAllRecords(String tid);
    List<UpdateRecord> readUpdateRecords();
    List<UpdateRecord> readUpdateRecords(String tid);
    void deleteAllRecords();
    void snipLog(int lsn);
    void dump();
}
