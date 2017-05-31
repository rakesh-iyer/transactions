class UpdateRecord extends LogRecord {
    Block block;
    Data oldData;
    Data newData;

    UpdateRecord(String transactionId) {
        super(transactionId);
    }

    Block getBlock() {
        return block;
    }

    void setBlock(Block block) {
        this.block = block;
    }

    Data getOldData() {
        return oldData;
    }

    void setOldData(Data oldData) {
        this.oldData = oldData;
    }

    Data getNewData() {
        return newData;
    }

    void setNewData(Data newData) {
        this.newData = newData;
    }

    public String toString() {
        return getBlock() + ":" +  getOldData() + ":" + getNewData() + ":" + super.toString();
    }
}
