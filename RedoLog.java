import java.util.*;

class RedoLog implements Log {
    LogImpl logImpl;
    Database db;

    RedoLog(LogImpl logImpl, Database db) {
        this.logImpl = logImpl;
        this.db = db;
    }

    public String startTransaction() {
        return UUID.randomUUID().toString();
    }

    public void write(Block b, Data d) {
        LogRecord r = new LogRecord();
        r.setBlock(b);
        r.setNewData(d);

        logImpl.writeRecord(r);
    }

    public void commitTransaction(String tid) {
        List<LogRecord> list = logImpl.readAllRecords();

        for (LogRecord r : list) {
            db.write(r.getBlock(), r.getNewData());
        }
        logImpl.deleteAllRecords();
    }

    public void abortTransaction(String tid) {
        logImpl.deleteAllRecords();
    }

    public void dump() {
        logImpl.dump();
    }

    public void dbdump() {
        db.dump();
    }
}
