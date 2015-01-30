package com.futureprocessing.mongojuggler.write;


import com.futureprocessing.mongojuggler.exception.InvalidNumberOfDocumentsAffected;
import com.mongodb.WriteResult;

public class UpdateResult {

    private final int affectedCount;

    public UpdateResult(WriteResult result) {
        affectedCount = result.getN();
    }


    public int getAffectedCount() {
        return affectedCount;
    }

    public void ensureOneUpdated() {
        ensureUpdated(1);
    }

    public void ensureUpdated(int expected) {
        if (affectedCount != expected) {
            throw new InvalidNumberOfDocumentsAffected(affectedCount, expected);
        }
    }
}
