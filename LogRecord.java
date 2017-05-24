import java.io.*;

class LogRecord implements Serializable {
    Block s;
    Data oldData;
    Data newData;
    Time t;

    Block getBlock() {
        return s;
    }

    void setBlock(Block s) {
        this.s = s;
    }

    Data getOldData() {
        return oldData;
    }

    void setOldData(Data d) {
        oldData = d;
    }

    Data getNewData() {
        return newData;
    }

    void setNewData(Data d) {
        newData = d;
    }

    Time getTime() {
        return t;
    }

    void setTime(Time t) {
        this.t = t;
    }

    public String toString() {
        return getBlock() + ":" +  getOldData() + ":" + getNewData() + ":" + getTime();
    }
}
