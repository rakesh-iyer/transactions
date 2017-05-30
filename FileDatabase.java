import java.util.*;

class FileDatabase implements Database {
    LogType logType;
    LogImpl logImpl = new FileLogImpl(UUID.randomUUID().toString());
    Datastore ds = new MapDatastore();

    FileDatabase(LogType logType) {
        this.logType = logType;
    }

    // transaction support.
    public String startTransaction() {
        return UUID.randomUUID().toString();
    }

    public Data read(Block b) {
        if (logType == LogType.UNDO) {
            return ds.read(b);
        } else {
            Data d = null;
            List<LogRecord> list = logImpl.readAllRecords();

            for (LogRecord r : list) {
                if (r.getBlock().equals(b)) {
                    d = r.getNewData();
                    break;
                }
            }

            if (d == null) {
                d = ds.read(b);
            }

            return d;
        }
    }

    public void write(Block b, Data d, String tid) {
        LogRecord r = LogRecord.newLogRecord();
        r.setTransactionId(tid);
        r.setBlock(b);
        r.setNewData(d); 

        if (logType == LogType.UNDO) {
            Data oldData = ds.read(b);
            r.setOldData(oldData);
        }

        logImpl.writeRecord(r);

        if (logType == LogType.UNDO) {
            ds.write(b, d, r.getLSN());
        }
    }

    public void commitTransaction(String tid) {
        
    }

    public void abortTransaction(String tid) {
    }

    // debugging

    public void dump() {
        System.out.println("dump starting");
        logImpl.dump();
        ds.dump();
        System.out.println("dump ending");
    }
}
