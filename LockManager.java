interface LockManager<T> {
    void acquireReaderLock(T t) throws InterruptedException;
    void acquireWriterLock(T t) throws InterruptedException;
    void releaseReaderLock(T t) throws InterruptedException;
    void releaseWriterLock(T t);
    void dump();
}
