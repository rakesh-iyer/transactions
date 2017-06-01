import java.io.*;
import java.util.*;

class LogRecord implements Serializable {
    Integer lsn;
    String transactionId;
    static int logRecordCount = 0;

    LogRecord(String transactionId) {
        synchronized(this) {
            lsn = logRecordCount++;
        }

        this.transactionId = transactionId;
    }

    Integer getLSN() {
        return lsn;
    }

    String getTransactionId() {
        return transactionId;
    }

    public String toString() {
        return getLSN() + ":" + getTransactionId();
    }
}
