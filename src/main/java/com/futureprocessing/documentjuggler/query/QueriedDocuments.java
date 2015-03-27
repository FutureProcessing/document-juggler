package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.update.RemoveResult;
import com.futureprocessing.documentjuggler.update.UpdateResult;

import java.util.List;
import java.util.function.Consumer;

public interface QueriedDocuments<MODEL> extends ReadQueriedDocuments<MODEL> {
    @Override
    MODEL first(String... fieldsToFetch);

    @Override
    List<MODEL> all(String... fieldsToFetch);

    @Override
    ReadQueriedDocuments<MODEL> skip(int skip);

    @Override
    ReadQueriedDocuments<MODEL> limit(int limit);

    UpdateResult update(Consumer<MODEL> consumer);

    RemoveResult remove();
}
