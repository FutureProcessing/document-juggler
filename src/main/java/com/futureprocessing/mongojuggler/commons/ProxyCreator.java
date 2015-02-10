package com.futureprocessing.mongojuggler.commons;

import com.futureprocessing.mongojuggler.read.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.read.ReadProxy;
import com.futureprocessing.mongojuggler.write.InsertMapper;
import com.futureprocessing.mongojuggler.write.InsertProxy;
import com.futureprocessing.mongojuggler.write.UpdateBuilder;
import com.futureprocessing.mongojuggler.write.UpdateProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Set;

import static java.lang.reflect.Proxy.newProxyInstance;

@SuppressWarnings("unchecked")
public class ProxyCreator {

    public static <READER> READER newReadProxy(Class<READER> readerType, ReadMapper mapper, DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                new ReadProxy(readerType, mapper, (BasicDBObject) dbObject, fields));
    }

    public static <UPDATER> UPDATER newInsertProxy(Class<UPDATER> updaterClass, InsertMapper mapper) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass}, new InsertProxy(updaterClass, mapper));
    }

    public static <UPDATER> UPDATER newUpdateProxy(Class<UPDATER> updaterClass, UpdateBuilder updateBuilder) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                new UpdateProxy(updaterClass, updateBuilder));
    }

    public static <QUERY> QUERY newQueryProxy(Class<QUERY> queryClass) {
        return (QUERY) newProxyInstance(queryClass.getClassLoader(), new Class[]{queryClass}, new QueryProxy());
    }
}
