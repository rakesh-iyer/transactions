import java.io.*;
import java.util.*;

class FileLogImpl implements LogImpl {
    File logFile;
    ObjectOutputStream oos;

    public FileLogImpl(String fileName) {
        logFile = new File(fileName);
        deleteAllRecords();
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
            oos.writeObject(r);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Unexpected io exception");
            e.printStackTrace();
        }
    }

    synchronized public List<LogRecord> readRecordsFilter(String tid, boolean onlyUpdates) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(logFile));
            List<LogRecord> list = new ArrayList<LogRecord>();
            boolean done = false;
            int i = 0;

            while (!done) {
                try {
                    LogRecord r = (LogRecord)ois.readObject();
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

    public List<LogRecord> readTransactionRecords(String tid) {
        return readRecordsFilter(tid, false);
    }

    public List<UpdateRecord> readUpdateRecords() {
        List<UpdateRecord> list = new ArrayList<>();
        for (LogRecord r : readRecordsFilter(null, true)) {
            list.add((UpdateRecord)r);
        }

        // no way to get base function to return a wildcard type.
        return list;
    }

    public List<LogRecord> readAllRecords() {
        return readRecordsFilter(null, false);
    }

    public void dump() {
        List<LogRecord> list = readAllRecords();

        for (LogRecord r : list) {
            System.out.println(r);
        }
    }
}
