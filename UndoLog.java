import java.util.*;

class UndoLog implements Log {
    LogImpl logImpl;
    Database db;

    public UndoLog(LogImpl logImpl, Database db) {
        this.logImpl = logImpl;
        this.db = db;
    }

    public String startTransaction() {
        String transactionId = UUID.randomUUID().toString();
        return transactionId;
    }

    public void write(Block s, Data newData) {
        Data oldData = db.read(s);

        LogRecord r = new LogRecord();
        r.setBlock(s);
        r.setOldData(oldData);
        r.setNewData(newData); 
        logImpl.writeRecord(r);

        db.write(s, newData);
    }

    public void abortTransaction(String tid) {
        List<LogRecord> recordList = logImpl.readAllRecords();
        for (int i = recordList.size() - 1; i >= 0; i--) {
            LogRecord record = recordList.get(i);
            // skip if there was no old data.
            if (record.getOldData() != null) {
                db.write(record.getBlock(), record.getOldData());
            }
        }
        logImpl.deleteAllRecords();
    }

    public void commitTransaction(String tid) {
        logImpl.deleteAllRecords();
    }

    public void dump() {
        logImpl.dump();
    }

    public void dbdump() {
        db.dump();
    }
}

