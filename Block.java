import java.io.*;

class Block implements Serializable {
    long block;

    Block(long block) {
        this.block = block;
    }

    public String toString() {
        return String.valueOf(block);
    }

    public int hashCode() {
        return new Long(block).hashCode();
    }

    public boolean equals(Object b) {
        return block == ((Block)b).block;
    }
}
