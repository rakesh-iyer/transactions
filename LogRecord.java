import java.io.*;

interface LogRecord extends Serializable {
    // enums static by definition.
    enum Type {
        REDO,
        UNDO,
        COMMIT,
        ABORT,
        COMPENSATION
    }

    Type getType();
    void performOperation() throws Exception;
}

interface UpdateRecord extends LogRecord {
}

interface StatusRecord extends LogRecord {
} 

class RedoRecord implements UpdateRecord {
    public Type getType() {
        return Type.REDO;
    }

    public void performOperation() throws Exception {
        // redo the steps listed in this record.
    }
}

class UndoRecord implements UpdateRecord {
    public Type getType() {
        return Type.UNDO;
    }

    public void performOperation() throws Exception {
        // undo steps listed in this record.
    }
}

class CommitRecord implements StatusRecord {
    public Type getType() {
        return Type.COMMIT;
    }

    public void performOperation() throws Exception {
        // ensure transaction is commited to disk?
    }
}

class AbortRecord implements StatusRecord {
    public Type getType() {
        return Type.ABORT;
    }

    public void performOperation() throws Exception {
        // ensure transaction is aborted ?
    }
}

class CompensationRecord implements UpdateRecord {
    public Type getType() {
        return Type.COMPENSATION;
    }

    public void performOperation() throws Exception {
        // ensure compensation is performed.
    }
}
