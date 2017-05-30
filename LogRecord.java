import java.io.*;
import java.util.*;

class LogRecord implements Serializable {
    String lsn;
    String transactionId;
    Block block;
    Data oldData;
    Data newData;

    private LogRecord() {
    }

    static LogRecord newLogRecord() {
        LogRecord r = new LogRecord();

        r.setLSN(UUID.randomUUID().toString());

        return r;
    }

    String getLSN() {
        return lsn;
    }

    private void setLSN(String lsn) {
        this.lsn = lsn;
    }

    String getTransactionId() {
        return transactionId;
    }

    void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    Block getBlock() {
        return block;
    }

    void setBlock(Block s) {
        this.block = block;
    }

    Data getOldData() {
        return oldData;
    }

    void setOldData(Data d) {
        oldData = d;
    }

    Data getNewData() {
        return newData;
    }

    void setNewData(Data d) {
        newData = d;
    }

    public String toString() {
        return getBlock() + ":" +  getOldData() + ":" + getNewData() + ":" + getLSN() + ":" + getTransactionId();
    }
}
