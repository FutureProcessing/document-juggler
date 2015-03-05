package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.futureprocessing.mongojuggler.insert.InserterConsumer;
import com.futureprocessing.mongojuggler.insert.InserterMapper;
import com.futureprocessing.mongojuggler.query.QueriedDocuments;
import com.futureprocessing.mongojuggler.query.QuerierConsumer;
import com.futureprocessing.mongojuggler.query.QuerierMapper;
import com.futureprocessing.mongojuggler.query.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReaderMapper;
import com.futureprocessing.mongojuggler.update.UpdaterMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class Repository<INSERTER, QUERIER, READER, UPDATER> {

    private final Class<INSERTER> inserterClass;
    private final Class<QUERIER> querierClass;
    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;

    private final InserterMapper inserterMapper;
    private final QuerierMapper querierMapper;
    private final ReaderMapper readerMapper;
    private final UpdaterMapper updaterMapper;

    private final DBCollection dbCollection;

    public Repository(DBCollection dbCollection,
                      Class<INSERTER> inserterClass,
                      Class<QUERIER> querierClass,
                      Class<READER> readerClass,
                      Class<UPDATER> updaterClass) {
        this.dbCollection = dbCollection;

        this.inserterClass = inserterClass;
        this.querierClass = querierClass;
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;

        this.inserterMapper = new InserterMapper(inserterClass);
        this.querierMapper = new QuerierMapper(querierClass);
        this.readerMapper = new ReaderMapper(readerClass);
        this.updaterMapper = new UpdaterMapper(updaterClass);
    }

    public QueriedDocuments<READER, UPDATER> find(QuerierConsumer<QUERIER> querierConsumer) {
        QUERIER querier = QueryProxy.create(querierClass, querierMapper.get(querierClass));
        querierConsumer.accept(querier);

        return new QueriedDocuments<>(readerClass, updaterClass, readerMapper, updaterMapper, dbCollection,
                QueryProxy.extract(querier).toDBObject());
    }

    public QueriedDocuments<READER, UPDATER> find() {
        return new QueriedDocuments<>(readerClass, updaterClass, readerMapper, updaterMapper, dbCollection, null);
    }

    public String insert(InserterConsumer<INSERTER> consumer) {
        INSERTER inserter = InsertProxy.create(inserterClass, inserterMapper.get(inserterClass));
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();
        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}
