import java.io.*;
import java.util.*;

class LogRecord implements Serializable {
    Integer lsn;
    String transactionId;
    static int logRecordCount = 0;

    LogRecord(String transactionId) {
        this.transactionId = transactionId;
    }

    Integer getLSN() {
        return lsn;
    }

    void setLSN(Integer lsn) {
        this.lsn = lsn;
    }

    String getTransactionId() {
        return transactionId;
    }

    public String toString() {
        return getLSN() + ":" + getTransactionId();
    }
}
