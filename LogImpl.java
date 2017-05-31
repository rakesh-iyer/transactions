import java.util.*;

interface LogImpl {
    void writeRecord(LogRecord r);
    List<LogRecord> readAllRecords();
    List<UpdateRecord> readUpdateRecords();
    List<LogRecord> readTransactionRecords(String tid);
    void deleteAllRecords();
    void dump();
}
