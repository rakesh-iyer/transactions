class StoredData {
    Data data;
    String lsn;

    Data getData() {
        return data;
    }

    void setData(Data data) {
        this.data = data;
    }

    String getLSN() {
        return lsn;
    }

    void setLSN(String lsn) {
        this.lsn = lsn;
    }

    public String toString() {
        return getData() + ":" + getLSN();
    }
}
