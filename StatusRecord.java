class StatusRecord extends LogRecord {
    boolean commit; // true is commit, false is abort.

    StatusRecord(String transactionId) {
        super(transactionId);
    }

    boolean isCommited() {
        return commit;
    }

    void setCommited(boolean commit) {
        this.commit = commit;
    }

    public String toString() {
        return String.valueOf(commit) + ":" + super.toString();
    }
}
