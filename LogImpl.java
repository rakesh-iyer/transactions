import java.util.*;

interface LogImpl {
    void writeRecord(LogRecord r);
    List<LogRecord> readAllRecords();
    void deleteAllRecords();
    void dump();
}
