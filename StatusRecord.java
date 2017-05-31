class StatusRecord extends LogRecord {
    boolean commit; // true is commit, false is abort.

    StatusRecord(String transactionId) {
        super(transactionId);
    }

    boolean isCommited() {
        return commit;
    }

    public String toString() {
        return String.valueOf(commit) + ":" + super.toString();
    }
}
