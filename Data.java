import java.io.*;

class Data implements Serializable {
    String s;

    Data(String d) {
        s = d;
    }

    public String toString() {
        return s;
    }
}
