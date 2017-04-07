import java.io.*;

interface LogImpl {
    boolean writeNextRecord(LogRecord record);
    LogRecord readNextRecord();
}

class LogImplFile {
    private String fileName;
    private FileInputStream fis;
    private FileOutputStream fos;

    void printException(Exception e) {
    }

    LogImplFile(String fileName) {
        this.fileName = fileName;
        try {
            fis = new FileInputStream(fileName);
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (fis !== null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }    

    boolean writeNextRecord(LogRecord record) {
        // write to the file at next position.
        try {
            ObjectOutputStream oos =  new ObjectOutputStream(fos);  
            oos.writeObject(record);
        } catch (IOException e) {
            printException(e);
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
            printException(e);
        } catch (ClassNotFoundException e) {
            printException(e);
        }

        return record;
    }
}
