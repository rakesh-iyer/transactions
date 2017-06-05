import java.io.*;
import java.nio.file.*;
import java.util.*;

class FileLogImpl implements LogImpl {
    File logFile;
    ObjectOutputStream oos;
    Integer lastLSN = 0;

    public FileLogImpl(String fileName) {
        logFile = new File(fileName);
        deleteAllRecords();
    }

    public int getFirstLSN(String tid) {
        List<LogRecord> list = readAllRecords(tid);

        return list.size() > 0 ? list.get(0).getLSN() : Integer.MAX_VALUE;
    }

    synchronized public void deleteAllRecords() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(logFile));  
        } catch (IOException e) {
            System.out.println("Unexpected io exception");
            e.printStackTrace();
        }
    }

    synchronized public void writeRecord(LogRecord r) {
        try {
            // generate lsn on write. this write has to be serialized to ensure a sequential log.
            synchronized(this) {
                r.setLSN(lastLSN++);
                oos.writeObject(r);
            }
            oos.flush();
        } catch (IOException e) {
            System.out.println("Unexpected io exception");
            e.printStackTrace();
        }
    }

    synchronized List<LogRecord> readRecordsFilter(String tid, int startLSN, boolean onlyUpdates) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(logFile));
            List<LogRecord> list = new ArrayList<LogRecord>();
            boolean done = false;
            int i = 0;

            while (!done) {
                try {
                    LogRecord r = (LogRecord)ois.readObject();

                    if (r.getLSN() < startLSN) {
                        continue;
                    }

                    if (tid != null && !r.getTransactionId().equals(tid)) {
                        continue;
                    }

                    if (onlyUpdates && r instanceof UpdateRecord == false) {
                        continue;
                    }

                    list.add(r);
                } catch (EOFException e) {
                    // done processing the log file.
                    done = true;
                } catch (ClassNotFoundException e) {
                    // done processing the log file.
                    System.out.println("Unexpected class not found exception");
                    done = true;
                }
            }

            ois.close();
            return list;
        } catch (IOException e) {
            System.out.println("Unexpected io exception");
            e.printStackTrace();
        }

        return null;
    }

    public List<LogRecord> readAllRecords() {
        return readRecordsFilter(null, 0, false);
    }

    public List<LogRecord> readAllRecords(String tid) {
        return readRecordsFilter(tid, 0, false);
    }

    public List<UpdateRecord> readUpdateRecords() {
        List<UpdateRecord> list = new ArrayList<>();
        for (LogRecord r : readRecordsFilter(null, 0, true)) {
            list.add((UpdateRecord)r);
        }

        // no way to get base function to return a wildcard type.
        return list;
    }

    public List<UpdateRecord> readUpdateRecords(String tid) {
        List<UpdateRecord> list = new ArrayList<>();
        for (LogRecord r : readRecordsFilter(tid, 0, true)) {
            list.add((UpdateRecord)r);
        }

        // no way to get base function to return a wildcard type.
        return list;
    }

    synchronized public void snipLog(int lsn) {
        try {
            oos.close();

            List<LogRecord> list = readRecordsFilter(null, lsn, false);
            File fileBackup = new File(logFile.getName() + ".backup" + System.currentTimeMillis());

            Files.move(logFile.toPath(), fileBackup.toPath(), StandardCopyOption.ATOMIC_MOVE);

            oos = new ObjectOutputStream(new FileOutputStream(logFile));
            for (LogRecord r : list) {
                writeRecord(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalize() {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getString(LogRecord r) {
        if (r instanceof UpdateRecord) {
            return "UpdateRecord";
        } else if (r instanceof CompensationRecord) {
            return "CompensationRecord";
        } else if (r instanceof StatusRecord) {
            return "StatusRecord";
        } else {
            return "LogRecord";
        }
    }

    public void dump() {
        System.out.println("FileLogImpl dump begins");
        List<LogRecord> list = readAllRecords();

        for (LogRecord r : list) {
            System.out.println(getString(r) + ":" + r);
        }
        System.out.println("FileLogImpl dump ends");
    }
}
