package com.futureprocessing.mongojuggler.commons;


import com.futureprocessing.mongojuggler.read.QueryProxy;
import com.futureprocessing.mongojuggler.write.InsertProxy;
import com.futureprocessing.mongojuggler.write.UpdateProxy;

import static java.lang.reflect.Proxy.getInvocationHandler;

public final class ProxyExtractor {

    public static QueryProxy extractQueryProxy(Object query) {
        return (QueryProxy) getInvocationHandler(query);
    }

    public static UpdateProxy extractUpdateProxy(Object updater) {
        return (UpdateProxy) getInvocationHandler(updater);
    }

    public static InsertProxy extractInsertProxy(Object updater) {
        return (InsertProxy) getInvocationHandler(updater);
    }

    private ProxyExtractor() {
    }

}
