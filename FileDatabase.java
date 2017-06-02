import java.util.*;
import java.util.concurrent.*;

class FileDatabase implements Database {
    LogType logType;
    LogImpl logImpl = new FileLogImpl(UUID.randomUUID().toString());
    Datastore ds = new MapDatastore();
    LockManager<Block> blockLockMgr = new MapLockManager<>();
    ConcurrentMap<String, List<Block>> transactionReadLocksMap = new ConcurrentHashMap<>();

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

    public void write(Block b, Data d, String tid, boolean append) {
        blockLockMgr.acquireWriterLock(b);
        Data oldData = ds.read(b);

        UpdateRecord r = new UpdateRecord(tid);
        r.setBlock(b);
        if (append) {
            d = Data.append(oldData, d);
        }
        r.setNewData(d);

        if (logType == LogType.UNDO) {
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
        abortTransaction(tid, false);
    }

    private void abortTransaction(String tid, boolean inRecovery) {
        // you could release the reader locks here in normal processing.
        if (!inRecovery) {
            releaseReaderLocks(tid);
        }

        // add the compensation records.
        List<UpdateRecord> list = logImpl.readUpdateRecords(tid);

        for (UpdateRecord r : list) {
            if (logType == LogType.UNDO) {
                CompensationRecord c = new CompensationRecord(r.getTransactionId());
                c.setBlock(r.getBlock());
                c.setData(r.getOldData());
                logImpl.writeRecord(c);

                /* In recovery mode this will happen in 2nd redo phase. */
                if (!inRecovery) {
                    ds.write(r.getBlock(), r.getOldData(), c.getLSN());
                }
            }
            if (!inRecovery) {
                blockLockMgr.releaseWriterLock(r.getBlock());
            }
        }

        StatusRecord s = new StatusRecord(tid);
        s.setCommited(false);
        logImpl.writeRecord(s);
    }

    private void redoRecovery(List<LogRecord> list, List<String> tids) {
        // redo the committed and aborted parts of the log. no locking needed as the serialization is decided.
        for (LogRecord r : list) {
            if (tids.contains(r.getTransactionId())) {
                if (r instanceof UpdateRecord) {
                    UpdateRecord u = (UpdateRecord) r;
                    ds.write(u.getBlock(), u.getNewData(), u.getLSN());
                } else if (r instanceof CompensationRecord) {
                    CompensationRecord c = (CompensationRecord) r;
                    ds.write(c.getBlock(), c.getData(), c.getLSN());
                }
            }
        }
    }

    private void undoRecovery(List<String> tids) {
        // anything that was not commited or aborted needs to have comp records and a abort record.
        for (String tid: tids) {
            abortTransaction(tid, true);
        }
    }

    // crash recovery using log.
    // The records commited/aborted are serialized ahead of ones that arent.
    // the undo may add partial transactions that need abort processing.
    // The recovery should be idempotent.
    public void recover() {
        List<LogRecord> list = logImpl.readAllRecords();
        Map<String, Integer> tids = new HashMap<>();

        for (LogRecord r : list) {
            String tid = r.getTransactionId();

            if (tids.get(tid) == null) {
               tids.put(tid, 0);
            }

            if (r instanceof StatusRecord) {
               tids.put(tid, 1);
            }
        }

        List<String> completedTransactions = new ArrayList<>();
        List<String> incompleteTransactions = new ArrayList<>();

        for (String tid : tids.keySet()) {
            if (tids.get(tid) == 1) {
                completedTransactions.add(tid);
            } else {
                incompleteTransactions.add(tid);
            }
        }

        redoRecovery(list, completedTransactions);
        undoRecovery(incompleteTransactions);
        redoRecovery(list, incompleteTransactions);
    }


    // debugging

    public void dump() {
        System.out.println("dump starting");
        System.out.println("Transaction Read Locks start");
        for (String tid : transactionReadLocksMap.keySet()) {
            System.out.println(tid + transactionReadLocksMap.get(tid));
        }        
        System.out.println("Transaction Read Locks end");
        blockLockMgr.dump();
        logImpl.dump();
        ds.dump();
        System.out.println("dump ending");
    }
}
