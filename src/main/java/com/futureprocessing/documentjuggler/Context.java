package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.annotation.internal.InsertCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.QueryCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.ReadCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateCommandProvider;

public enum Context {

    INSERT(InsertCommandProvider.class),
    QUERY(QueryCommandProvider.class),
    READ(ReadCommandProvider.class),
    UPDATE(UpdateCommandProvider.class);

    private Class contextAnnotationClass;

    private Context(Class contextAnnotationClass) {
        this.contextAnnotationClass = contextAnnotationClass;
    }

    public Class getContextAnnotationClass() {
        return contextAnnotationClass;
    }
}
