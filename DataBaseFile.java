package database;

interface DataBase {
    String read(ReadCommand cmd);
    void write(WriteCommand cmd);
}

class DataBaseFile implements DataBase {
    String fileName;
    FileInputStream fis;
    FileOutputStream fos;

    DataBaseFile(String fileName) {
        this.fileName = fileName;
    }

    String read(ReadCommand cmd) {
        // is blockNumber valid?
        // allocate byte array
        // read back from storage, return array.
       
    }

    void writeBlock(WriteCommand cmd) {
        // is blockNumber valid?
        // write to storage.
    }
}
