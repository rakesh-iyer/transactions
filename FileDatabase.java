import java.util.*;

class FileDatabase implements Database {
    LogType logType;
    LogImpl logImpl = new FileLogImpl(UUID.randomUUID().toString());
    Datastore ds = new MapDatastore();
    LockManager<Block> blockLockMgr = new MapLockManager<>();
    Map<String, List<Block>> transactionReadLocksMap = new HashMap<>();

    FileDatabase(LogType logType) {
        this.logType = logType;
    }

    // transaction support.
    public String startTransaction() {
        String tid = UUID.randomUUID().toString();
        transactionReadLocksMap.put(tid, new ArrayList<Block>());

        return tid;
    }

    public Data read(Block b, String tid) {
        blockLockMgr.acquireReaderLock(b);
        transactionReadLocksMap.get(tid).add(b);

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
        blockLockMgr.acquireWriterLock(b);
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

    private void releaseReaderLocks(String tid) {
        for (Block b : transactionReadLocksMap.get(tid)) {
            blockLockMgr.releaseReaderLock(b);
        }
    }

    public void commitTransaction(String tid) {
        StatusRecord s = new StatusRecord(tid);
        s.setCommited(true);
        logImpl.writeRecord(s);

        // you could release the reader locks here.
        releaseReaderLocks(tid);

        // now do the log updates
        List<UpdateRecord> list = logImpl.readUpdateRecords(tid);

        for (UpdateRecord r : list) {
            if (logType == LogType.REDO) {
                ds.write(r.getBlock(), r.getNewData(), r.getLSN());
            }
            blockLockMgr.releaseWriterLock(r.getBlock());
        }
    }

    public void abortTransaction(String tid) {
        // you could release the reader locks here.
        releaseReaderLocks(tid);

        // add the compensation records.
        List<UpdateRecord> list = logImpl.readUpdateRecords(tid);

        for (UpdateRecord r : list) {
            if (logType == LogType.UNDO) {
                CompensationRecord c = new CompensationRecord(r.getTransactionId());
                c.setBlock(r.getBlock());
                c.setData(r.getOldData());
                logImpl.writeRecord(c);

                ds.write(r.getBlock(), r.getOldData(), c.getLSN());
            }
            blockLockMgr.releaseWriterLock(r.getBlock());
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
