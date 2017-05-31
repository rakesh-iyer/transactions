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
}

