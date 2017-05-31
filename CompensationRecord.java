class CompensationRecord extends LogRecord {
    Block block;
    Data data;

    CompensationRecord(String transactionId) {
        super(transactionId);
    }

    Block getBlock() {
        return block;
    }

    void setBlock(Block block) {
        this.block = block;
    }

    Data getData() {
        return data;
    }

    void setData(Data data) {
        this.data = data;
    }

    public String toString() {
        return getBlock() + ":" +  getData() + ":" + super.toString();
    }
}
