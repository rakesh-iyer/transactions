import java.io.*;
import java.util.*;

class LogRecord implements Serializable {
    String lsn;
    String transactionId;

    LogRecord(String transactionId) {
        lsn = UUID.randomUUID().toString();
        this.transactionId = transactionId;
    }

    String getLSN() {
        return lsn;
    }

    String getTransactionId() {
        return transactionId;
    }

    public String toString() {
        return getLSN() + ":" + getTransactionId();
    }
}
