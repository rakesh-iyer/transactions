class CompensationRecord extends LogRecord {
    Block block;
    Data data;

    CompensationRecord(String transactionId) {
        super(transactionId);
    }

    Block getBlock() {
        return block;
    }

    void setBlock(Block s) {
        this.block = block;
    }

    Data getData() {
        return data;
    }

    void setData(Data d) {
        data = d;
    }

    public String toString() {
        return getBlock() + ":" +  getData() + ":" + super.toString();
    }
}
