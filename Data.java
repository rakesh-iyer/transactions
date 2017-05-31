import java.io.*;

class Data implements Serializable {
    String data;

    Data(String data) {
        this.data = data;
    }

    public String toString() {
        return data;
    }
}
