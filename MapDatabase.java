import java.util.*;

class MapDatabase implements Database {
    Map<Block, Data> dbMap;

    public MapDatabase() {
        dbMap = new HashMap<>();
    }

    public Data read(Block s) {
        return dbMap.get(s);
    }

    public void write(Block s, Data d) {
        dbMap.put(s, d);
    }

    public void dump() {
        for (Map.Entry<Block,Data> entry : dbMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    } 
}

