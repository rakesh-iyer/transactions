import java.util.*;

class MapDatastore implements Datastore {
    Map<Block, StoredData> dbMap;

    public MapDatastore() {
        dbMap = new HashMap<>();
    }

    public Data read(Block b) {
        StoredData sd = dbMap.get(b);
        if (sd == null) {
            sd = new StoredData();
            dbMap.put(b, sd);
        }

        return sd.getData();
    }

    public void write(Block b, Data d, String lsn) {
        StoredData sd = new StoredData();
        sd.setData(d);
        sd.setLSN(lsn);

        dbMap.put(b, sd);
    }

    public void dump() {
        for (Map.Entry<Block,StoredData> entry : dbMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    } 
}

