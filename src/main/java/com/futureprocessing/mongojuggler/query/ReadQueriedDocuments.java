package com.futureprocessing.mongojuggler.query;

import java.util.List;

public interface ReadQueriedDocuments<READER> {

    READER first(String... fieldsToFetch);

    List<READER> all(String... fieldsToFetch);

    ReadQueriedDocuments<READER> skip(int skip);

    ReadQueriedDocuments<READER> limit(int limit);
}
