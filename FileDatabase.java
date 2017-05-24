import java.util.*;

class FileDatabase implements Database {
    LogType logType;
    LogImpl logImpl = new FileLogImpl(UUID.randomUUID().toString());
    Datastore ds = new MapDatastore();

    FileDatabase(LogType logType) {
        this.logType = logType;
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

    public void write(Block b, Data d) {
        if (logType == LogType.UNDO) {
            Data oldData = ds.read(b);

            LogRecord r = new LogRecord();
            r.setBlock(b);
            r.setOldData(oldData);
            r.setNewData(d); 
            logImpl.writeRecord(r);

            ds.write(b, d);
        } else {
            LogRecord r = new LogRecord();
            r.setBlock(b);
            r.setNewData(d);

            logImpl.writeRecord(r);
        }
    }

    
    // transaction support.

    public String startTransaction() {
        return UUID.randomUUID().toString();
    }

    public void commitTransaction(String tid) {
        if (logType == LogType.UNDO) {
            logImpl.deleteAllRecords();
        } else {
            List<LogRecord> list = logImpl.readAllRecords();

            for (LogRecord r : list) {
                ds.write(r.getBlock(), r.getNewData());
            }
            logImpl.deleteAllRecords();
        }
    }

    public void abortTransaction(String tid) {
        if (logType == LogType.UNDO) {
            List<LogRecord> recordList = logImpl.readAllRecords();
            for (int i = recordList.size() - 1; i >= 0; i--) {
                LogRecord record = recordList.get(i);
                // skip if there was no old data.
                if (record.getOldData() != null) {
                    ds.write(record.getBlock(), record.getOldData());
                }
            }
            logImpl.deleteAllRecords();
        } else {
            logImpl.deleteAllRecords();
        }
    }

    // debugging

    public void dump() {
        System.out.println("dump starting");
        logImpl.dump();
        ds.dump();
        System.out.println("dump ending");
    }
}
