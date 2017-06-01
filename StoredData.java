class StoredData {
    Data data;
    Integer lsn;

    Data getData() {
        return data;
    }

    void setData(Data data) {
        this.data = data;
    }

    Integer getLSN() {
        return lsn;
    }

    void setLSN(Integer lsn) {
        this.lsn = lsn;
    }

    public String toString() {
        return getData() + ":" + getLSN();
    }
}
