
// Log is read and written sequentially
interface Log {
    void writeNextRecord(LogRecord record);
    LogRecord readNextRecord();
}

class LogImpl implements Log {
    void writeNextRecord(LogRecord record) {
        // determine current position in log.
        // write to Current position in log.
    }

    LogRecord 
