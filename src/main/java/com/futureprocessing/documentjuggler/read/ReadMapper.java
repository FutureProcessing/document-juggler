package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.annotation.AnnotationReader;
import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import static com.futureprocessing.documentjuggler.Context.READ;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public final class ReadMapper extends Mapper<ReadCommand> {

    public static <MODEL> ReadMapper map(Class<MODEL> modelClass) {
        ReadMapper mapper = new ReadMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private ReadMapper(Class clazz) {
        super(clazz, new DefaultReadCommandProvider());
    }

    @Override
    protected ReadCommand getCommand(Method method) {
        if (isForbidden(method, READ) || !hasCorrectParameters(method)) {
            return new ForbiddenReadCommand(method);
        }

        AnnotationReader annotationReader = from(method);

        String field = FieldNameExtractor.getFieldName(method);

        if (annotationReader.isPresent(DbEmbeddedDocument.class)) {
            return new EmbeddedReadCommandProvider().getCommand(method, this);
        }

        if (annotationReader.isPresent(AsObjectId.class)) {
            return new IdReadCommand(field);
        }

        return getDefaultCommand(method);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }
}
