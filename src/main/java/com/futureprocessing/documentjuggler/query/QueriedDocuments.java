package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.update.RemoveResult;
import com.futureprocessing.documentjuggler.update.UpdateResult;
import com.futureprocessing.documentjuggler.update.UpdatConsumer;

import java.util.List;

public interface QueriedDocuments<MODEL> extends ReadQueriedDocuments<MODEL> {
    @Override
    MODEL first(String... fieldsToFetch);

    @Override
    List<MODEL> all(String... fieldsToFetch);

    @Override
    ReadQueriedDocuments<MODEL> skip(int skip);

    @Override
    ReadQueriedDocuments<MODEL> limit(int limit);

    UpdateResult update(UpdatConsumer<MODEL> consumer);

    RemoveResult remove();
}
