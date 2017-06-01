import java.util.*;
import java.util.concurrent.*;

class MapDatastore implements Datastore {
    ConcurrentMap<Block, StoredData> dbMap;

    public MapDatastore() {
        dbMap = new ConcurrentHashMap<>();
    }

    public Data read(Block b) {
        StoredData sd = dbMap.get(b);
        if (sd == null) {
            sd = new StoredData();
            sd.setData(new Data("initial"));
            sd.setLSN(0);
            dbMap.put(b, sd);
        }

        return sd.getData();
    }

    public void write(Block b, Data d, Integer lsn) {
        StoredData sd = new StoredData();
        sd.setData(d);
        sd.setLSN(lsn);

        dbMap.put(b, sd);
    }

    public void dump() {
        System.out.println("MapDatastore dump begins");
        for (Map.Entry<Block,StoredData> entry : dbMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println("MapDatastore dump ends");
    } 
}

