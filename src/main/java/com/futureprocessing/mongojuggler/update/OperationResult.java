package com.futureprocessing.mongojuggler.update;

import com.futureprocessing.mongojuggler.exception.InvalidNumberOfDocumentsAffected;
import com.mongodb.WriteResult;

public abstract class OperationResult {
    private final int affectedCount;

    public OperationResult(WriteResult result) {
        affectedCount = result.getN();
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
