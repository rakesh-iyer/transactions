class RWLock {
    int readers = 0;
    Mutex readerLock = new Mutex();
    Mutex exclusiveLock = new Mutex();

    class Mutex {
        boolean lock;

        synchronized void acquire() throws InterruptedException {
            while (lock) {
                wait();
            }
            lock = true;
        }

        synchronized void release() {
            lock = false;
            notify();
        }

        public String toString() {
            return String.valueOf(lock);
        }
    }

    void acquireWriterLock() throws InterruptedException {
        exclusiveLock.acquire();
    }

    void releaseWriterLock() {
        exclusiveLock.release();
    }

    void acquireReaderLock() throws InterruptedException {
        readerLock.acquire();
        if (readers == 0) {
            exclusiveLock.acquire();
        }
        readers++;

        readerLock.release();
    }

    void releaseReaderLock() throws InterruptedException {
        readerLock.acquire();
        readers--;
        readerLock.release();
    }

    public String toString() {
        return readerLock.toString() + ":" + exclusiveLock.toString();
    }
}

