import java.util.*;

class MapLockManager<T> implements LockManager<T> {
    Map <T, RWLock> lockMap = new HashMap<>();

    private RWLock getRWLock(T t) {
        RWLock lock = lockMap.get(t);
        if (lock == null) {
            lock = new RWLock();
            lockMap.put(t, lock);
        }

        return lock;
    }

    public void acquireReaderLock(T t) {
        RWLock lock = getRWLock(t);

        lock.acquireReaderLock();
    }

    public void acquireWriterLock(T t) {
        RWLock lock = getRWLock(t);

        lock.acquireWriterLock();
    }

    public void releaseReaderLock(T t) {
        RWLock lock = getRWLock(t);

        lock.releaseReaderLock();
    }

    public void releaseWriterLock(T t) {
        RWLock lock = getRWLock(t);

        lock.releaseWriterLock();
    }
}
