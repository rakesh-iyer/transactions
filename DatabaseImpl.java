interface Database {
    byte[] readBlock(int blockNumber);
    void writeBlock(byte[] array, int blockNumber);
}

class DataBaseImpl {
    byte[] readBlock(int blockNumber) {
        // is blockNumber valid?
        // allocate byte array
        // read back from storage, return array.
    }

    void writeBlock(byte[] array, int blockNumber) {
        // is blockNumber valid?
        // write to storage.
    }
}
