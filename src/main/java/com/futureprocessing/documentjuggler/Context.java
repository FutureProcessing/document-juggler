package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.annotation.internal.InsertContext;
import com.futureprocessing.documentjuggler.annotation.internal.QueryContext;
import com.futureprocessing.documentjuggler.annotation.internal.ReadContext;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateContext;

public enum Context {

    INSERT(InsertContext.class),
    QUERY(QueryContext.class),
    READ(ReadContext.class),
    UPDATE(UpdateContext.class);

    private Class contextAnnotationClass;

    private Context(Class contextAnnotationClass) {
        this.contextAnnotationClass = contextAnnotationClass;
    }

    public Class getContextAnnotationClass() {
        return contextAnnotationClass;
    }
}
