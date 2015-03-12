package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.insert.InserterConsumer;
import com.futureprocessing.documentjuggler.query.QueriedDocuments;
import com.futureprocessing.documentjuggler.query.QuerierConsumer;

public interface Repository<MODEL> {
    QueriedDocuments<MODEL> find(QuerierConsumer<MODEL> querierConsumer);

    QueriedDocuments<MODEL> find();

    String insert(InserterConsumer<MODEL> consumer);
}
