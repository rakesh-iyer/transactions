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
            // no compensation records needed right?
            Data d = null;
            List<UpdateRecord> list = logImpl.readUpdateRecords();

            for (UpdateRecord r : list) {
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
        UpdateRecord r = new UpdateRecord(tid);
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
        StatusRecord s = new StatusRecord(tid);
        s.setCommited(true);
        logImpl.writeRecord(s);

        // now do the log updates
        if (logType == LogType.REDO) {
            List<UpdateRecord> list = logImpl.readUpdateRecords(tid);

            for (UpdateRecord r : list) {
                ds.write(r.getBlock(), r.getNewData(), r.getLSN());
            }
        }
    }

    public void abortTransaction(String tid) {
        // add the compensation records.
        if (logType == LogType.UNDO) {
            List<UpdateRecord> list = logImpl.readUpdateRecords(tid);

            for (UpdateRecord r : list) {
                CompensationRecord c = new CompensationRecord(r.getTransactionId());
                c.setBlock(r.getBlock());
                c.setData(r.getOldData());
                logImpl.writeRecord(c);

                ds.write(r.getBlock(), r.getOldData(), c.getLSN());
            }
        }

        StatusRecord s = new StatusRecord(tid);
        s.setCommited(false);
        logImpl.writeRecord(s);
    }

    // debugging

    public void dump() {
        System.out.println("dump starting");
        logImpl.dump();
        ds.dump();
        System.out.println("dump ending");
    }
}
