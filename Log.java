class Log {
    private LogImpl impl;

    Log(LogImpl impl) {
        this.impl = impl;
    }

    LogImpl getLogImpl() {
        return impl;
    }

    boolean writeNextRecord(LogRecord record) {
        return impl.writeNextRecord(record);
    }

    LogRecord readNextRecord() {
        return impl.readNextRecord();
    }
}

