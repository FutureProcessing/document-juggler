package com.futureprocessing.documentjuggler.update.command.providers;

import com.futureprocessing.documentjuggler.annotation.AnnotationReader;
import com.futureprocessing.documentjuggler.annotation.UnsetIfFalse;
import com.futureprocessing.documentjuggler.annotation.UnsetIfNull;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.BasicUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.BooleanUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.FieldNameExtractor.getFieldName;

public class DefaultUpdateCommandProvider implements CommandProvider<UpdateCommand> {
    @Override
    public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
        AnnotationReader annotationReader = from(method);
        final String field = getFieldName(method);

        Class parameterClass = method.getParameterTypes()[0];
        if (parameterClass.equals(boolean.class) || parameterClass.equals(Boolean.class)) {
            return new BooleanUpdateCommand(field,
                    annotationReader.isPresent(UnsetIfNull.class),
                    annotationReader.isPresent(UnsetIfFalse.class));
        }

        return new BasicUpdateCommand(field, annotationReader.isPresent(UnsetIfNull.class));
    }
}
