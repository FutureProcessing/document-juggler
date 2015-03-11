package com.futureprocessing.documentjuggler.update;

import com.mongodb.WriteResult;

public class RemoveResult extends OperationResult {
    public RemoveResult(WriteResult result) {
        super(result);
    }

    public void ensureOneDeleted() {
        ensureAffected(1);
    }

    public void ensureDeleted(int expected) {
        ensureAffected(expected);
    }
}
