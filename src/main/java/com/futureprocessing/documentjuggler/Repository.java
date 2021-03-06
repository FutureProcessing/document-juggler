package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.commons.CollectionExtractor;
import com.futureprocessing.documentjuggler.insert.InsertConsumer;
import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.query.QueriedDocuments;
import com.futureprocessing.documentjuggler.query.QueriedDocumentsImpl;
import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.futureprocessing.documentjuggler.query.expression.QueryExpression;
import com.futureprocessing.documentjuggler.read.ReadProcessor;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.bson.types.ObjectId;

public class Repository<MODEL> {

    private final DBCollection dbCollection;

    private ReadProcessor<MODEL> readProcessor;
    private QueryProcessor<MODEL> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public Repository(DB db, Class<MODEL> modelClass) {
        this(CollectionExtractor.getDBCollection(db, modelClass), modelClass);
    }

    public Repository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        this.readProcessor = new ReadProcessor<>(modelClass, dbCollection);
        this.queryProcessor = new QueryProcessor<>(modelClass);
        this.insertProcessor = new InsertProcessor<>(modelClass);
        this.updateProcessor = new UpdateProcessor<>(modelClass);
    }

    public QueriedDocuments<MODEL> find(QueryConsumer<MODEL> consumer) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(consumer), readProcessor, updateProcessor);
    }

    public QueriedDocuments<MODEL> find(QueryExpression<MODEL> expression) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(expression), readProcessor, updateProcessor);
    }

    public QueriedDocuments<MODEL> find() {
        return find((QueryConsumer<MODEL>) null);
    }

    public String insert(InsertConsumer<MODEL> consumer) {
        BasicDBObject document = insertProcessor.process(consumer);


        dbCollection.insert(document);
        if (document.get("_id") instanceof ObjectId) {
            return document.getObjectId("_id").toHexString();
        }
        return document.get("_id").toString();
    }
}
