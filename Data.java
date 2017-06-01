import java.io.*;

class Data implements Serializable {
    String data;

    Data(String data) {
        this.data = data;
    }

    public static Data append(Data d, Data a) {
        return new Data(d.toString() + "-" + a.toString());
    }

    public String toString() {
        return data;
    }
}
