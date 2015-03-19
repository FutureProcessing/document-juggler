package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.insert.InsertConsumer;
import com.futureprocessing.documentjuggler.query.QueriedDocuments;
import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryExpression;

public interface Repository<MODEL> {
    QueriedDocuments<MODEL> find(QueryConsumer<MODEL> queryConsumer);

    QueriedDocuments<MODEL> find(QueryExpression<MODEL> queryExpression);

    QueriedDocuments<MODEL> find();

    String insert(InsertConsumer<MODEL> consumer);
}
