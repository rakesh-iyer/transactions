interface LockManager<T> {
    void acquireReaderLock(T t);
    void acquireWriterLock(T t);
    void releaseReaderLock(T t);
    void releaseWriterLock(T t);
    void dump();
}
