package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.commons.Metadata;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UpdateEmbeddedProxy implements InvocationHandler {

    private final UpdateProxy parentProxyHandler;
    private final String parentField;

    public UpdateEmbeddedProxy(UpdateProxy parentProxyHandler, String parentField) {
        this.parentProxyHandler = parentProxyHandler;
        this.parentField = parentField;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String field = Metadata.getFieldName(method);
        parentProxyHandler.setFieldValue(method, parentField + "." + field, args);
        return proxy;
    }

}
