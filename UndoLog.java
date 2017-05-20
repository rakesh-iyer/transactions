import java.util.*;

class UndoLog implements Log {
    LogImpl impl;

    UndoLog(LogImpl impl) {
        logImpl = impl;
    }

    String startTransaction() {
        String transactionId = UUID.randomUUID().toString();
        return transactionId;
    }

    void write(Sector s, Data newData) {
        oldData = db.read(s);
        LogRecord r = new LogRecord(s, oldData, newData); 
        logImpl.writeRecord(r);
        db.write(s, newData);
    }

    void abortTransaction() {
        List<LogRecord> recordList = logImpl.readAllRecords();
        for (int i = recordList.size()-1; i >= 0; i--) {
            LogRecord record = recordList.get(i);
            db.write(record.getSector(), record.getOldData());
        }
        logImpl.deleteAllRecords();
    }

    void commitTransaction() {
        logImpl.deleteAllRecords();
    }
}

