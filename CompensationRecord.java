class CompensationRecord extends LogRecord {
    Block block;
    Data oldData;
    Data newData;

    CompensationRecord(String transactionId) {
        super(transactionId);
    }

    Block getBlock() {
        return block;
    }

    void setBlock(Block s) {
        this.block = block;
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

    public String toString() {
        return getBlock() + ":" +  getOldData() + ":" + getNewData() + ":" + super.toString();
    }
}
