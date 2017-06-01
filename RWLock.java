class RWLock {
    int readers = 0;
    Mutex readerLock = new Mutex();
    Mutex exclusiveLock = new Mutex();

    class Mutex {
        boolean lock;

        synchronized void acquire() {
            while (lock) {
                try {
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    void acquireWriterLock() {
        exclusiveLock.acquire();
    }

    void releaseWriterLock() {
        exclusiveLock.release();
    }

    void acquireReaderLock() {
        readerLock.acquire();
        if (readers == 0) {
            exclusiveLock.acquire();
        }
        readers++;

        readerLock.release();
    }

    void releaseReaderLock() {
        readerLock.acquire();
        readers--;
        readerLock.release();
    }

    public String toString() {
        return readerLock.toString() + ":" + exclusiveLock.toString();
    }
}

