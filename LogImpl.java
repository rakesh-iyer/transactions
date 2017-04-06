import java.io.*;

interface LogImpl {
    boolean writeNextRecord(LogRecord record);
    LogRecord readNextRecord();
}

class LogImplFile {
    private String fileName;
    private FileInputStream fis;
    private FileOutputStream fos;

    LogImplFile(String fileName) {
        this.fileName = fileName;
        try {
            fis = new FileInputStream(fileName);
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
        }
    }    

    boolean writeNextRecord(LogRecord record) {
        // write to the file at next position.
        try {
            ObjectOutputStream oos =  new ObjectOutputStream(fos);  
            oos.writeObject(record);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    LogRecord readNextRecord() {
        LogRecord record = null;
        // read from the file at next position.
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            record = (LogRecord)ois.readObject();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }

        return record;
    }
}
