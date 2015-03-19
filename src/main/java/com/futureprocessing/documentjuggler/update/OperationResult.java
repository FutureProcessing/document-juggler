package com.futureprocessing.documentjuggler.update;

import com.futureprocessing.documentjuggler.exception.InvalidNumberOfDocumentsAffected;
import com.mongodb.WriteResult;

public abstract class OperationResult {
    private final int affectedCount;

    public OperationResult(WriteResult result) {
        affectedCount = result != null ? result.getN() : 0;
    }

    public int getAffectedCount() {
        return affectedCount;
    }

    protected void ensureAffected(int expected) {
        if (affectedCount != expected) {
            throw new InvalidNumberOfDocumentsAffected(affectedCount, expected);
        }
    }
}
