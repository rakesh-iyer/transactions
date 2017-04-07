package database;

class ReadCommand {
    String tableId;
    String primaryKey;
    String attr;
}

class WriteCommand {
    String tableId;
    String primaryKey;
    String attr;
    String value;
}
