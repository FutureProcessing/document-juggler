package com.futureprocessing.mongojuggler.commons;

import com.futureprocessing.mongojuggler.read.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReadProxy;
import com.futureprocessing.mongojuggler.write.InsertProxy;
import com.futureprocessing.mongojuggler.write.UpdateEmbeddedProxy;
import com.futureprocessing.mongojuggler.write.UpdateProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.Set;

import static java.lang.reflect.Proxy.newProxyInstance;

@SuppressWarnings("unchecked")
public class ProxyCreator {

    public static <READER> READER newReadProxy(Class<READER> readerType, DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                new ReadProxy(readerType, (BasicDBObject) dbObject, fields));
    }

    public static <UPDATER> UPDATER newInsertProxy(Class<UPDATER> updaterClass) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass}, new InsertProxy(updaterClass));
    }

    public static <UPDATER> UPDATER newUpdateProxy(Class<UPDATER> updaterClass, DBCollection collection, DBObject updateQuery) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                new UpdateProxy(collection, updateQuery));
    }

    public static <UPDATER> UPDATER newUpdateEmbeddedProxy(Class<UPDATER> updaterClass, UpdateProxy parentProxyHandler, String field) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                new UpdateEmbeddedProxy(parentProxyHandler, field));
    }

    public static <QUERY> QUERY newQueryProxy(Class<QUERY> queryClass) {
        return (QUERY) newProxyInstance(queryClass.getClassLoader(), new Class[]{queryClass}, new QueryProxy());
    }
}
